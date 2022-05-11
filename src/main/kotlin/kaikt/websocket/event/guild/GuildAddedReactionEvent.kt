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
)