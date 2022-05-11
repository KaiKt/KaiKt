package kaikt.websocket.event.direct

import kaikt.websocket.KaiClient

data class PrivateUpdatedMessageEvent(
	val client: KaiClient,

	val content: String,
	val authorId: String,
	val messageId: String,
	val updatedTimestamp: Long,
	val chatCode: String
)