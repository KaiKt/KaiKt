package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildDeletedBlockListEvent(
	val client: KaiClient,

	val guildId: String,
	val operatorId: String,
	val userIds: List<String>
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val operator by lazy { client.acorn.createAcornUser(operatorId) }
	val unblockedUsers by lazy { userIds.map { client.acorn.createAcornUser(it) } }
}
