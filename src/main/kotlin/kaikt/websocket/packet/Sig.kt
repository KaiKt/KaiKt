package kaikt.websocket.packet

enum class Sig(val value: Int) {

	/**
	 * 未知信令
	 */
	UNKNOWN(-1),

	/**
	 * 消息信令
	 */
	DATA(0),

	/**
	 * 欢迎包
	 */
	HELLO(1),

	/**
	 * 客户端到服务器心跳包
	 */
	PING(2),

	/**
	 * 服务器到客户端心跳包
	 */
	PONG(3),

	/**
	 * RESUME 包
	 */
	RESUME(4),

	/**
	 * 重连包
	 */
	RECONNECT(5),

	/**
	 * RESUME_ACK 包
	 */
	RESUME_ACK(6),
	;

	companion object {
		fun get(value: Int) = entries.firstOrNull { it.value == value }
	}
}
