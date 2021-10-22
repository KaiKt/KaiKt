package kaikt.websocket.acorn

import kaikt.websocket.acorn.entity.AcornMessageRequest

interface AcornMessage {

	/**
	 * 消息ID
	 */
	fun getId(): String

	/**
	 * 消息内容
	 */
	fun getMessage(): String

	/**
	 * 消息发送的时间戳
	 */
	fun getMessageTimestamp(): Long

	/**
	 * 回复消息
	 * @param content 消息内容
	 */
	fun reply(content: String, request: AcornMessageRequest.() -> Unit = {}): AcornMessage

	/**
	 * 添加[emojiId]回应
	 */
	fun addReaction(emojiId: String): Boolean

	/**
	 * 移除[emojiId]回应
	 */
	fun delReaction(emojiId: String): Boolean

}