package kaikt.websocket

import com.google.gson.Gson
import com.google.gson.JsonParser
import kaikt.api.KaiApi
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.c2s.C2SPingPacket
import kaikt.websocket.packet.s2c.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Logger
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.logging.Level
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

private val gson = Gson()
private val logger = LoggerFactory.getLogger("KaiClient")

@OptIn(ExperimentalTime::class, DelicateCoroutinesApi::class)
open class KaiClient(val api: KaiApi): WebSocketClient(api.Gateway().getGateway().data.getURI()) {

	val packetBus: EventBus = EventBus.builder().logger(object : Logger {
		override fun log(level: Level, message: String) = log(level, message, null)
		override fun log(level: Level, message: String, throwable: Throwable?) {
			when(level) {
				Level.FINE -> logger.trace(message)
				Level.INFO -> logger.info(message)
				Level.WARNING -> logger.warn(message)
				Level.SEVERE -> if(throwable == null) {
					logger.error(message)
				} else {
					logger.error(message, throwable)
				}
			}
		}
	}).build()

	var keepAlive = true
	var reconnecting = false

	var welcomeReceived = false
	var heartbeatReceived = false

	val packetHandler = PacketHandler(this)
	val eventBus: EventBus get() = packetHandler.bus

	val me = api.meUser

	fun start() {
		connect()
	}

	override fun onOpen(handshakedata: ServerHandshake?) {
		launchWelcomePacketCountDown()
	}

	/**
	 * 开始 WelcomePacket 倒计时，6秒后没收到就运行 [onWelcomeTimedOut]
	 */
	private fun launchWelcomePacketCountDown() = GlobalScope.launch {
		welcomeReceived = false
		delay(seconds(6))
		if(!welcomeReceived) {
			onWelcomeTimedOut()
		} else {
			launchHeartbeat()
		}
	}

	private fun launchHeartbeat() = GlobalScope.launch {
		while(keepAlive) {
			heartbeatReceived = false
			sendPacket(C2SPingPacket(packetHandler.sn))
			delay(seconds(6))
			if(!heartbeatReceived) {
				this.cancel("未收到来自服务器的心跳！")
				onHeartbeatTimedOut()
			}
			delay(seconds(30))
		}
	}

	override fun onMessage(message: String) {
		logger.debug("[S2C] $message")

		val json = JsonParser.parseString(message)
		val type = kotlin.runCatching { json.asJsonObject.getAsJsonPrimitive("s").asInt }.onFailure {
			logger.warn("无法解析来自远程的消息（缺少信令）：$message")
		}.getOrThrow()

		when(type) {
			0 -> { // 普通事件
				val packet = gson.fromJson(json, S2CEventPacket::class.java)
				packetBus.post(packet)
				packetHandler.handle(packet)
			}
			1 -> { // Welcome
				val packet = gson.fromJson(json, S2CHelloPacket::class.java)
				packetBus.post(packet)
				welcomeReceived = true
				logger.info("客户端启动成功，开始监听！（Welcome）")
			}
			2 -> {
				logger.warn("收到了本地的数据包，这是一个非预期的行为：$json")
			}
			3 -> { // 收到来自服务器的心跳 PONG
				val packet = gson.fromJson(json, S2CPongPacket::class.java)
				packetBus.post(packet)
				heartbeatReceived = true
				logger.debug("收到来自服务器的心跳。（Reconnect = $reconnecting）")

				// 重连
				if(reconnecting) {
					reconnecting = false
					launchHeartbeat()
					logger.info("重连成功！")
				}
			}
			5 -> { // 收到来自服务器的重连要求
				val packet = gson.fromJson(json, S2CReconnectPacket::class.java)
				packetBus.post(packet)
			}
			6 -> { // S2CResumeAckPacket
				val packet = gson.fromJson(json, S2CResumeAckPacket::class.java)
				packetBus.post(packet)
			}
			else -> {
				logger.warn("未解析的数据包（信令=$type）：$json")
			}
		}
	}

	override fun onClose(code: Int, reason: String?, remote: Boolean) {
		println("$code, $reason, $remote")
		packetHandler.resetSn()
	}

	override fun onError(ex: Exception?) {
		println("$ex")
	}

	/**
	 * 向服务器发送数据包
	 */
	fun sendPacket(packet: Packet) {
		val json = gson.toJson(packet)
		this.send(json)
		logger.debug("[C2S] $json")
	}

	open fun onWelcomeTimedOut() {
		exitProcess(-1)
	}

	open fun onHeartbeatTimedOut() = GlobalScope.launch {
		reconnecting = true

		logger.info("开始重连")

		sendPacket(C2SPingPacket(packetHandler.sn))
		delay(seconds(2))
		if(reconnecting) {
			sendPacket(C2SPingPacket(packetHandler.sn))
			delay(seconds(2))
			if(reconnecting) {
				logger.warn("重连失败！")
			}
		}
	}

}