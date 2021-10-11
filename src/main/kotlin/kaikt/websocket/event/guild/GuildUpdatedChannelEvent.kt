package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KChannelDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.*

data class GuildUpdatedChannelEvent(
	val client: KaiClient,

	val guildId: String,
	val channelDefinition: KChannelDefinition
) {

	val guild get() = HGuild(client.api, guildId)

	val channel get() = channelDefinition.toHChannel(client.api, guild)

}
