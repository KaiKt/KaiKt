package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildDeletedChannelEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String
)