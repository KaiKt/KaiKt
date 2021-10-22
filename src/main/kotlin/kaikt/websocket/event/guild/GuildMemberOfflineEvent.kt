package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildMemberOfflineEvent(
	val client: KaiClient,

	val guildId: String,
	val userId: String,
	val offlineAt: Long,
	val guildIds: List<String>
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val user by lazy { client.acorn.createAcornUser(userId) }
}
