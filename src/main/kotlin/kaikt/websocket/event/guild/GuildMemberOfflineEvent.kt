package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.guild.HGuild

data class GuildMemberOfflineEvent(
	val client: KaiClient,

	val guildId: String,
	val userId: String,
	val offlineAt: Long,
	val guildIds: List<String>
) {

	val guild get() = HGuild(client.api, guildId)

	val user get() = HUser(client.api, userId, guild)

}
