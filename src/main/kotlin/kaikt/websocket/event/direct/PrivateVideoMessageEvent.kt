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
)