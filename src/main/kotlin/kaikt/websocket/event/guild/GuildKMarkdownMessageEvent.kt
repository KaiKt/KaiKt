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
)