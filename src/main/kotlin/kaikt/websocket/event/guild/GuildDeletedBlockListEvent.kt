package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.guild.HGuild

data class GuildDeletedBlockListEvent(
	val client: KaiClient,

	val guildId: String,
	val operatorId: String,
	val userIds: List<String>
) {

	val guild get() = HGuild(client.api, guildId)

	val operator get() = HUser(client.api, operatorId, guild)

	val unblockedUsers get() = userIds.map { HUser(client.api, it) }

}
