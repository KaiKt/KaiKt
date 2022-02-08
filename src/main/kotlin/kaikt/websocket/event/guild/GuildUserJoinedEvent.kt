package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUserJoinedEvent(
	val client: KaiClient,

	val guildId: String,
	val userId: String,
	val joinedAt: Long
)