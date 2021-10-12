package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KGuildDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.HGuild

data class GuildDeletedGuildEvent(
	val client: KaiClient,

	val guildId: String,
	val guildDefinition: KGuildDefinition
) {

	val guild get() = HGuild(client.api, guildId)

}
