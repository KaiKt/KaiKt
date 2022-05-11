package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KEmojiDefinition
import kaikt.websocket.KaiClient

data class PrivateAddedReactionEvent(
	val client: KaiClient,

	// Extra
	val chatCode: String,
	val emoji: KEmojiDefinition,
	val userId: String,
	val messageId: String
)
