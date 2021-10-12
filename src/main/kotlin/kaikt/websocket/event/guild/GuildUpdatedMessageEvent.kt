package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.*

data class GuildUpdatedMessageEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String,
	val updatedContent: String,
	val mention: List<String>,
	val mentionAll: Boolean,
	val mentionHere: Boolean,
	val mentionRoles: List<String>,
	val messageId: String
) {

	val guild get() = HGuild(client.api, guildId)

	val channel get() = HChannel(client.api, guild, channelId)

	val updatedMessage get() = HGuildMessage(client.api, 1, channel, messageId, updatedContent, null)

}