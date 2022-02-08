package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildDeletedMessageEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String,
	val messageId: String
)