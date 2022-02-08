package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUnpinnedMessageEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String,
	val operatorId: String,
	val messageId: String
)