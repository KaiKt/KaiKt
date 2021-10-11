package kaikt.websocket.event.guild

import kaikt.api.entity.definition.FileAttachment
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.guild.*
import kaikt.websocket.hazelnut.toHUser

data class GuildFileMessageEvent(
	val client: KaiClient,

	val imageUrl: String,
	val authorId: String,
	val channelId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val guildId: String,
	val channelName: String,
	val mention: List<String>,
	val mentionAll: Boolean,
	val mentionHere: Boolean,
	val mentionRoles: List<String>,
	val author: KUserDefinition,
	val attachments: FileAttachment
) {

	val guild get() = HGuild(client.api, guildId)
	val channel get() = HChannel(client.api, guild, channelId)

	val sender get() = author.toHUser(client.api, guild)

	val message get() = HGuildMessage(client.api, 2, channel, messageId, imageUrl, sender)
}