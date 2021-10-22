package kaikt.websocket.event.direct

import kaikt.websocket.KaiClient

data class PrivateUpdatedMessageEvent(
	val client: KaiClient,

	val content: String,
	val authorId: String,
	val messageId: String,
	val updatedTimestamp: Long,
	val chatCode: String
) {

	val messageSender by lazy { client.acorn.createAcornUser(authorId) }
	val updatedMessage by lazy { client.acorn.buildAcornMessage {
		messageId = this@PrivateUpdatedMessageEvent.messageId
		source = messageSender
		messageContent = this@PrivateUpdatedMessageEvent.content
		messageTimestamp = this@PrivateUpdatedMessageEvent.updatedTimestamp
	} }
}