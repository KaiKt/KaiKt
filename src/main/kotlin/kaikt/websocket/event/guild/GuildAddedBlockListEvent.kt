package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildAddedBlockListEvent(
	val client: KaiClient,

	val guildId: String,
	val operatorId: String,
	val remark: String,
	val userIds: List<String>
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val operatorUser by lazy { client.acorn.createAcornUser(operatorId) }
	val blockedUsers by lazy { userIds.map { client.acorn.createAcornUser(it) } }
}
