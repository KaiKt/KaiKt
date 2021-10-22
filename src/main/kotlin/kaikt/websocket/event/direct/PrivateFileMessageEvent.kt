package kaikt.websocket.event.direct

import kaikt.api.entity.definition.FileAttachment
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

data class PrivateFileMessageEvent(
	val client: KaiClient,

	val fileUrl: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition,
	val attachments: FileAttachment
) {
	val authorUser by lazy { client.acorn.createAcornUser(authorId) }
	val targetUser by lazy { client.acorn.createAcornUser(targetId) }
	val message by lazy { client.acorn.buildAcornMessage {
		messageId = this@PrivateFileMessageEvent.messageId
		source = authorUser
		messageContent = this@PrivateFileMessageEvent.fileUrl
		messageTimestamp = this@PrivateFileMessageEvent.messageTimestamp
	} }
}