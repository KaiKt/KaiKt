package kaikt

import kaikt.api.KToken
import kaikt.api.KaiApi
import kaikt.websocket.KaiClient
import kaikt.websocket.packet.s2c.S2CEventPacket

fun main() {
	val token = System.getenv("TOKEN")
	if(token == null) {
		println("请设置环境变量 TOKEN")
		return
	}

	object : KaiClient(KaiApi(KToken(KToken.TokenType.Bot, token))) {
		override suspend fun processEvent(packet: S2CEventPacket) {
			println(packet)
		}
	}.initialize()
}