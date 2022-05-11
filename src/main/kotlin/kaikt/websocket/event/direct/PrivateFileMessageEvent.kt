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
)