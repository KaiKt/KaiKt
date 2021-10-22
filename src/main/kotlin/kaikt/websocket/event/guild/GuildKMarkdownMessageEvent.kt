package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KMarkdownDefinition
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

data class GuildKMarkdownMessageEvent(
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
	val kMarkdown: KMarkdownDefinition
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val channel by lazy { client.acorn.createAcornChannel(channelId) }
	val sender by lazy { client.acorn.createAcornUser(authorId) }
	val message by lazy { client.acorn.buildAcornMessage {
		this.messageId = this@GuildKMarkdownMessageEvent.messageId
		this.source = channel
		this.messageContent = content
		this.messageTimestamp = this@GuildKMarkdownMessageEvent.messageTimestamp
	} }
}