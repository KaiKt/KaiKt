package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

data class PrivateTextMessageEvent(
	val client: KaiClient,

	val content: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition
) {
	val authorUser by lazy { client.acorn.createAcornUser(authorId) }
	val targetUser by lazy { client.acorn.createAcornUser(targetId) }
	val message by lazy { client.acorn.buildAcornMessage {
		messageId = this@PrivateTextMessageEvent.messageId
		source = authorUser
		messageContent = this@PrivateTextMessageEvent.content
		messageTimestamp = this@PrivateTextMessageEvent.messageTimestamp
	} }
}