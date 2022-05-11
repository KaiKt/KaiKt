package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

@Deprecated("开黑啦已经移除 Text 消息，并全部转为 KMarkdown 消息", replaceWith = ReplaceWith("GuildKMarkdownMessageEvent"))
data class GuildTextMessageEvent(
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
	val author: KUserDefinition
)