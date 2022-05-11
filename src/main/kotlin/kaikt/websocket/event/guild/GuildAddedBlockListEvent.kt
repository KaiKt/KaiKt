package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildAddedBlockListEvent(
	val client: KaiClient,

	val guildId: String,
	val operatorId: String,
	val remark: String,
	val userIds: List<String>
)