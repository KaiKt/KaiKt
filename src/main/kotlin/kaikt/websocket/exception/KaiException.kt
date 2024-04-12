package kaikt.websocket.exception

import kaikt.websocket.packet.s2c.S2CHelloPacket
import kaikt.websocket.packet.s2c.S2CPongPacket
import kaikt.websocket.packet.c2s.C2SPingPacket

sealed class KaiException : Exception()

/**
 * 当在客户端与服务器连接后的规定时间内没有收到 [S2CHelloPacket] 时抛出
 */
data object UnwelcomeException : KaiException() {
	private fun readResolve(): Any = UnwelcomeException
}

/**
 * 当在客户端发送 [心跳包][C2SPingPacket] 后的规定时间内没有收到 [返回的心跳包][S2CPongPacket] 时抛出
 */
data object HeartAttackException : KaiException() {
	private fun readResolve(): Any = HeartAttackException
}

/**
 * 当多次没有收到心跳包并且重连失败后抛出
 */
data object BrainDeadException : KaiException() {
	private fun readResolve(): Any = BrainDeadException
}

/**
 * 当收到服务器的 RECONNECT 信令时抛出
 *
 * 捕获到 [RemakeException] 时，应该重新创建一个客户端实例。
 */
data object RemakeException : KaiException() {
	private fun readResolve(): Any = RemakeException
}
