package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUserExitedEvent(
	val client: KaiClient,

	val guildId: String,
	val userId: String,
	val exitedAt: Long
)