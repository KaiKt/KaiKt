package kaikt.websocket.event.direct

import kaikt.websocket.KaiClient

data class PrivateDeletedMessageEvent(
	val client: KaiClient,

	val authorId: String,
	val messageId: String,
	val deletedTimestamp: Long,
	val chatCode: String
)