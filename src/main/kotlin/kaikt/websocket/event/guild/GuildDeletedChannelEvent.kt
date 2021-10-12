package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.HChannel
import kaikt.websocket.hazelnut.guild.HGuild

data class GuildDeletedChannelEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String
) {

	val guild get() = HGuild(client.api, guildId)

	val channel get() = HChannel(client.api, guild, channelId)

}
