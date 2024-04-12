package simplews

import kaikt.gson
import kaikt.websocket.packet.Sig
import kaikt.websocket.packet.s2c.S2CHelloPacket
import kaikt.websocket.packet.s2c.S2CPongPacket
import kaikt.websocket.packet.s2c.data.HelloPacketData
import kaikt.websocket.peekPacket
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.slf4j.LoggerFactory
import java.net.URI

private val Names = arrayOf(
	"張偉",
	"王偉",
	"王芳",
	"李偉",
	"李娜",
	"張敏",
	"李靜",
	"王靜",
	"劉偉",
)

class SimpleWebSocketServer : WebSocketServer() {

	private val logger = LoggerFactory.getLogger("SimpleWebSocketServer")

	internal var lastWs: WebSocket? = null

	override fun onStart() {
		logger.info("服务器正在监听 $port")
	}

	internal fun WebSocket.send(data: Any) = send(gson.toJson(data))

	internal val WebSocket.name get() = Names[hashCode() % Names.size]

	private val takenNames = mutableSetOf<String>()

	override fun onOpen(ws: WebSocket, handshake: ClientHandshake) {
		if(ws.name in takenNames) {
			ws.close(999)
			logger.info("${ws.name} 发生重名，已经断开第二个连接")
			return
		}

		logger.info("${ws.name} 加入连接，发送欢迎包")
		takenNames += ws.name
		ws.send(S2CHelloPacket(1, HelloPacketData(0, "")))
		lastWs = ws
	}

	override fun onClose(ws: WebSocket, code: Int, reason: String, remote: Boolean) {
		logger.info("${ws.name} 断开连接")
		takenNames -= ws.name
	}

	override fun onMessage(ws: WebSocket, message: String) {
		logger.info("${ws.name}：$message")

		val p = peekPacket(message)

		// send pong when receiving ping
		if(p.isType(Sig.PING)) {
			ws.send(S2CPongPacket(Sig.PONG.value))
		}
	}

	override fun onError(conn: WebSocket, ex: Exception) {
		throw ex
	}

	companion object {
		val DefaultURI = URI("ws://127.0.0.1:80")
	}
}

fun main() {
	println("SIMPLE")
	val serv = SimpleWebSocketServer().apply { start() }

	while(true) {
		val s = readlnOrNull() ?: continue

		if(s == "quit" || s == "exit") break

		if(s.startsWith("send")) {
			val message = s.removePrefix("send").trimStart()
			val ws = serv.lastWs ?: continue
			ws.send(message)
			println("0")
		}

		if(s.startsWith("close")) {
			val ws = serv.lastWs ?: continue
			val reason = s.removePrefix("close").trimStart()
			ws.close(4000, reason)
			println("0")
		}
	}
}