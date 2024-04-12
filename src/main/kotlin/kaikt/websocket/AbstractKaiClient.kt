@file:Suppress("MemberVisibilityCanBePrivate") @file:JvmName("KaiClientKt")

package kaikt.websocket

import com.google.gson.JsonElement
import kaikt.api.KaiApi
import kaikt.api.entity.definition.KUserDefinition
import kaikt.gson
import kaikt.websocket.KaiClient.ExtensionMethods.to
import kaikt.websocket.exception.BrainDeadException
import kaikt.websocket.exception.HeartAttackException
import kaikt.websocket.exception.RemakeException
import kaikt.websocket.exception.UnwelcomeException
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.Sig
import kaikt.websocket.packet.c2s.C2SPingPacket
import kaikt.websocket.packet.c2s.C2SResumePacket
import kaikt.websocket.packet.s2c.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.jetbrains.annotations.TestOnly
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val WelcomeTimeout = System.getProperty("kai.welcomeTimeout")?.toIntOrNull() ?: 6
private val HeartbeatTimeout = System.getProperty("kai.heartbeatTimeout")?.toIntOrNull() ?: 6
private val HeartbeatCooldown = System.getProperty("kai.heartbeatCooldown")?.toIntOrNull() ?: 30
private val MaxReconnectRetry = System.getProperty("kai.maxReconnectRetry")?.toIntOrNull() ?: 3
private val ProcessMyMessage = System.getProperty("kai.processMyMessage")?.toBooleanStrictOrNull() ?: false

private val ProcessDebugSpells = System.getProperty("kai.processDebugSpells")?.toBooleanStrictOrNull() ?: false
private val DebugSpellPrelude = System.getProperty("kai.debugSpellPrelude") ?: "我は令咒の名で傀儡を命令する，"

/**
 * Kook 客户端
 *
 * [Kook官方状态图](https://developer.kookapp.cn/img/state.png)
 */
abstract class KaiClient(val api: KaiApi) {

	private val coroutineScope = CoroutineScope(EmptyCoroutineContext) + CoroutineName("KaiClient Main")

	private val logger = LoggerFactory.getLogger("KaiClient")
	private val s2cMarker = MarkerFactory.getMarker("S2C")
	private val c2sMarker = MarkerFactory.getMarker("C2S")
	private val hbMarker = MarkerFactory.getMarker("Pacemaker")
	private val dbgMarker = MarkerFactory.getMarker("DebugSpell")

	private val helloChannel = Channel<S2CHelloPacket>()
	private val pongChannel = Channel<S2CPongPacket>()

	private val snCounter = SnCounter(0)

	val me get() = _me
	private lateinit var _me: KUserDefinition

	private val wsClient = object : WebSocketClient(api.Gateway().getGateway().data.getURI()) {
		override fun onOpen(handshake: ServerHandshake?) {
		}

		override fun onMessage(message: String?) {
			if(message == null) return logger.warn("收到了空消息")
			// 启动协程来处理信息
			coroutineScope.launch(Dispatchers.IO + CoroutineName("Message Proc") + CoroutineExceptionHandler { ctx, ex ->
				logger.warn("处理消息时发生错误！", ex)
				logger.warn("相关上下文：$ctx")
			}) {
				handleMessage(message)
			}
		}

		override fun onClose(code: Int, reason: String?, remote: Boolean) {
		}

		override fun onError(ex: Exception) {
			throw ex
		}
	}

	fun initialize() {
		logger.info("正在初始化")

		// 启动 ws 客户端
		wsClient.connect()

		// 获取机器人信息
		_me = api.User().getUserMe().data

		// 在 6s 内获取欢迎，否则抛出超时异常
		coroutineScope.launch(CoroutineName("Welcome Supplicant")) {
			acquireHelloPacket(WelcomeTimeout.seconds)
			logger.info("成功与服务器建立连接")

			// 启动心跳包
			coroutineScope.launch(CoroutineName("Pacemaker")) {
				while(true) {
					sendHeartbeat()
					try {
						acquirePongPacket(HeartbeatTimeout.seconds)
					} catch(e: HeartAttackException) {
						// 发生超时后进入重新连接阶段
						logger.warn(hbMarker, "丢失来自服务器的心跳包，正在尝试重新连接")
						for(tryCount in 0..<MaxReconnectRetry) { // 尝试3次
							logger.info(hbMarker, "第 ${tryCount + 1} 次重新连接...")
							sendHeartbeat()
							try {
								acquirePongPacket(HeartbeatTimeout.seconds)
								break // 如果成功收到心跳包就跳出重连机制
							} catch(e: HeartAttackException) {
								if(tryCount == MaxReconnectRetry - 1) { // 第三次失败后直接抛出异常
									logger.warn(hbMarker, "重新连接失败")
									throw BrainDeadException
								}
								continue // 否则继续重试
							}
						}
						logger.info(hbMarker, "重新连接成功")
					}
					delay(HeartbeatCooldown.seconds)
				}
			}
		}
	}

	private suspend fun handleMessage(message: String) {
		logger.debug(s2cMarker, message)

		val peek = peekPacket(message)
		if(!peek.valid) {
			logger.warn("收到了无效的消息：$message")
			return
		}
		val rawData = peek.getData()

		when(peek.getType()) {
			Sig.DATA -> { // 0
				val packet = rawData.to<S2CEventPacket>()
				onDataPacket(packet)
			}

			Sig.HELLO -> { // 1
				val packet = rawData.to<S2CHelloPacket>()
				onHelloPacket(packet)
			}

			Sig.PING -> { // 2
				logger.warn("收到了非预期的 PING 的消息：$rawData")
			}

			Sig.PONG -> { // 3
				val packet = rawData.to<S2CPongPacket>()
				onRemoteHeartbeatPacket(packet)
			}

			Sig.RESUME -> { // 4
				logger.warn("收到了非预期的 RESUME 的消息：$rawData")
			}

			Sig.RECONNECT -> { // 5
				val packet = rawData.to<S2CReconnectPacket>()
				onReconnectPacket(packet)
			}

			Sig.RESUME_ACK -> { // 6
				val packet = rawData.to<S2CResumeAckPacket>()
				onResumeAckPacket(packet)
			}

			else -> {
				logger.warn("收到了非预期信令的消息：$rawData")
			}
		}
	}

	private suspend fun onDataPacket(packet: S2CEventPacket) {
		if(packet.sn > snCounter.expectedSn) {
			logger.warn("收到了来自未来的消息 ${packet.sn}，尝试重新获取此消息之前的消息（${snCounter.expectedSn} - ${packet.sn}）")
			sendResume(snCounter.sn)
			return
		}
		if(packet.sn < snCounter.expectedSn) {
			logger.info("收到了过去的消息 ${packet.sn}，已放弃不再处理")
			return
		}
		if(packet.sn == snCounter.expectedSn) {
			snCounter.incrementAndGet()

			// 当 [ProcessMyMessage] 为 [false] 时，
			// 跳过处理自己发送的消息
			if(!ProcessMyMessage && packet.data.authorId == me.id) {
				return
			}

			// 当 debug 指令功能启用时，处理 debug 指令，
			// 如果处理成功会返回 true，这时直接跳过这条消息的处理
			if(ProcessDebugSpells && processDebugSpells(packet)) {
				return
			}

			processEvent(packet)
		}
	}

	private suspend fun onHelloPacket(packet: S2CHelloPacket) {
		helloChannel.send(packet)
	}

	private suspend fun onRemoteHeartbeatPacket(packet: S2CPongPacket) {
		pongChannel.send(packet)
	}

	private fun onReconnectPacket(packet: S2CReconnectPacket) {
		throw RemakeException
	}

	private fun onResumeAckPacket(packet: S2CResumeAckPacket) {
		// TODO
	}

	/**
	 * 处理消息
	 *
	 * 其他基础系统事件和 DebugSpell 消息不会触发这个方法。
	 * 在这个方法中抛出异常不会导致客户端崩溃，但是仍然不建议直接抛出异常。
	 */
	abstract suspend fun processEvent(packet: S2CEventPacket)

	/**
	 * 在 [timeout] 时间内获取 [S2CHelloPacket]，否则抛出 [UnwelcomeException] 异常
	 */
	private suspend fun acquireHelloPacket(timeout: Duration): S2CHelloPacket {
		try {
			return withTimeout(timeout) {
				helloChannel.receive()
			}
		} catch(e: TimeoutCancellationException) {
			throw UnwelcomeException
		}
	}

	private suspend fun acquirePongPacket(timeout: Duration): S2CPongPacket {
		try {
			return withTimeout(timeout) {
				pongChannel.receive()
			}
		} catch(e: TimeoutCancellationException) {
			throw HeartAttackException
		}
	}

	private fun sendHeartbeat() {
		send(C2SPingPacket(snCounter.sn))
	}

	private fun sendResume(atSn: Int) {
		send(C2SResumePacket(atSn))
	}

	fun send(packet: Packet) {
		val data = gson.toJson(packet)
		logger.debug(c2sMarker, data)
		wsClient.send(data)
	}

	private inline fun String.letIfStartsWith(prefix: String, block: (String) -> Unit) {
		if(startsWith(prefix)) {
			substring(prefix.length).apply(block)
		}
	}

	private fun processDebugSpells(packet: S2CEventPacket): Boolean {

		val d = packet.data

		// 匹配前缀
		val msg = if(d.content.startsWith(DebugSpellPrelude)) {
			d.content.substring(DebugSpellPrelude.length)
		} else {
			return false
		}

		msg.letIfStartsWith("设置信令计数") {
			val newSn = it.trim().toIntOrNull()
			if(newSn != null) {
				logger.warn(dbgMarker, "设置信令计数从 ${snCounter.sn} 到 $newSn")
				snCounter.testSetSn(newSn)
				return true
			}
		}

		return false
	}

	data class SnCounter(@Volatile private var _sn: Int) {

		val sn get() = updater.get(this)
		val expectedSn get() = sn + 1

		fun incrementAndGet() = updater.incrementAndGet(this)

		@TestOnly
		internal fun testSetSn(sn: Int) = updater.set(this, sn)

		companion object {
			private val updater = AtomicIntegerFieldUpdater.newUpdater(SnCounter::class.java, "_sn")
		}

	}

	internal object ExtensionMethods {
		/**
		 * Convert the [JsonElement] to the desired instance [T].
		 */
		inline fun <reified T> JsonElement.to(): T = gson.fromJson(this, T::class.java)
	}

	companion object {

		init {
			val logger = LoggerFactory.getLogger("GameSettings Exporter")
			logger.debug("WelcomeTimeout = $WelcomeTimeout")
			logger.debug("HeartbeatTimeout = $HeartbeatTimeout")
			logger.debug("HeartbeatCooldown = $HeartbeatCooldown")
			logger.debug("MaxReconnectRetry = $MaxReconnectRetry")
			logger.debug("ProcessDebugSpells = $ProcessDebugSpells")
		}
	}

}