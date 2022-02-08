package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildDeletedBlockListEvent(
	val client: KaiClient,

	val guildId: String,
	val operatorId: String,
	val userIds: List<String>
)