package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.*

data class GuildUnpinnedMessageEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String,
	val operatorId: String,
	val messageId: String
) {

	val guild get() = HGuild(client.api, guildId)

	val channel get() = HChannel(client.api, guild, channelId)

	val message get() = HGuildMessage(client.api, 1, channel, messageId, null, null)

}
