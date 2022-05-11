package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUpdatedMessageEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String,
	val updatedContent: String,
	val mention: List<String>,
	val mentionAll: Boolean,
	val mentionHere: Boolean,
	val mentionRoles: List<String>,
	val messageId: String,
	val updatedAt: Long
)