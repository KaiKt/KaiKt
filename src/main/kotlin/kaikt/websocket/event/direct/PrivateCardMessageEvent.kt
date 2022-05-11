package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KMarkdownDefinition
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

data class PrivateCardMessageEvent(
	val client: KaiClient,

	val imageUrl: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition,
	val kMarkdown: KMarkdownDefinition
)