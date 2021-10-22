package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildDeletedChannelEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val channel by lazy { client.acorn.createAcornChannel(channelId) }
}
