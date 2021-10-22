package kaikt.websocket.event.direct

import kaikt.api.entity.definition.ImageAttachment
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

data class PrivateImageMessageEvent(
	val client: KaiClient,

	val imageUrl: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition,
	val attachments: ImageAttachment
) {
	val authorUser by lazy { client.acorn.createAcornUser(authorId) }
	val targetUser by lazy { client.acorn.createAcornUser(targetId) }
	val message by lazy { client.acorn.buildAcornMessage {
		messageId = this@PrivateImageMessageEvent.messageId
		source = authorUser
		messageContent = this@PrivateImageMessageEvent.imageUrl
		messageTimestamp = this@PrivateImageMessageEvent.messageTimestamp
	} }
}