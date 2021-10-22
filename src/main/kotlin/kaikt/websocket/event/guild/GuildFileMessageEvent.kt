package kaikt.websocket.event.guild

import kaikt.api.entity.definition.FileAttachment
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

data class GuildFileMessageEvent(
	val client: KaiClient,

	val content: String,
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

	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val channel by lazy { client.acorn.createAcornChannel(channelId) }
	val sender by lazy { client.acorn.createAcornUser(authorId) }
	val message by lazy { client.acorn.buildAcornMessage {
		this.messageId = this@GuildFileMessageEvent.messageId
		this.source = channel
		this.messageContent = content
		this.messageTimestamp = this@GuildFileMessageEvent.messageTimestamp
	} }
}