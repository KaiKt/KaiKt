package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KRoleDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.HGuild

data class GuildDeletedRoleEvent(
	val client: KaiClient,

	val guildId: String,
	val roleDefinition: KRoleDefinition
) {

	val guild get() = HGuild(client.api, guildId)

}
