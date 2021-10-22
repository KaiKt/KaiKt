package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KEmojiDefinition
import kaikt.websocket.KaiClient

data class GuildAddedReactionEvent(
	val client: KaiClient,

	// Extra
	val channelId: String,
	val emoji: KEmojiDefinition,
	val userId: String,
	val messageId: String
) {
	val sender by lazy { client.acorn.createAcornUser(userId) }
	val channel by lazy { client.acorn.createAcornChannel(channelId) }
	val message by lazy { client.acorn.createAcornMessage(messageId, channel) }
}
