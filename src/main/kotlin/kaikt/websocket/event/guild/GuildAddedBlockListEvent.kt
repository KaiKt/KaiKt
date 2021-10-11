package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.guild.HGuild

data class GuildAddedBlockListEvent(
	val client: KaiClient,

	val guildId: String,
	val operatorId: String,
	val remark: String,
	val userIds: List<String>
) {

	val guild get() = HGuild(client.api, guildId)

	val operator get() = HUser(client.api, operatorId, guild)

	val blockedUsers get() = userIds.map { HUser(client.api, it) }

}
