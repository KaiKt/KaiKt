package kaikt.websocket.event.direct

import kaikt.api.entity.definition.*
import kaikt.websocket.KaiClient

data class PrivateVideoMessageEvent(
	val client: KaiClient,

	val videoUrl: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition,
	val attachments: VideoAttachment
) {
	val authorUser by lazy { client.acorn.createAcornUser(authorId) }
	val targetUser by lazy { client.acorn.createAcornUser(targetId) }
	val message by lazy { client.acorn.buildAcornMessage {
		messageId = this@PrivateVideoMessageEvent.messageId
		source = authorUser
		messageContent = this@PrivateVideoMessageEvent.videoUrl
		messageTimestamp = this@PrivateVideoMessageEvent.messageTimestamp
	} }
}