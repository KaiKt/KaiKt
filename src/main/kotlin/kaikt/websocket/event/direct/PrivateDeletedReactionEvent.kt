package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KEmojiDefinition
import kaikt.websocket.KaiClient

data class PrivateDeletedReactionEvent(
	val client: KaiClient,

	// Extra
	val chatCode: String,
	val emoji: KEmojiDefinition,
	val userId: String,
	val messageId: String
) {
	val sender by lazy { client.acorn.createAcornUser(userId) }
	val message by lazy { client.acorn.createAcornMessage(messageId, sender) }
}
