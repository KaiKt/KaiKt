package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUserExitedEvent(
	val client: KaiClient,

	val guildId: String,
	val userId: String,
	val exitedAt: Long
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val user by lazy { client.acorn.createAcornUser(userId) }
}
