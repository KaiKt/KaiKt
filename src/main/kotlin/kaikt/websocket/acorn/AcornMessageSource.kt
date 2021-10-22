package kaikt.websocket.acorn

import kaikt.websocket.acorn.entity.AcornMessageRequest

interface AcornMessageSource {

	fun getId(): String

	/**
	 * 向用户发送消息
	 */
	fun sendMessage(content: String, request: AcornMessageRequest.() -> Unit = {}): AcornMessage

}