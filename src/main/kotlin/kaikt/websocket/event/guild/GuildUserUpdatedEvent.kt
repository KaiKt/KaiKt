package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUserUpdatedEvent(
	val client: KaiClient,

	val guildId: String,
	val userId: String,
	val nickname: String
)