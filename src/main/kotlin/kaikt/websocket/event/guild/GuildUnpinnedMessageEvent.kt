package kaikt.websocket.event.guild

import kaikt.websocket.KaiClient

data class GuildUnpinnedMessageEvent(
	val client: KaiClient,

	val guildId: String,
	val channelId: String,
	val operatorId: String,
	val messageId: String
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val channel by lazy { client.acorn.createAcornChannel(channelId) }
	val operator by lazy { client.acorn.createAcornUser(operatorId) }
	val message by lazy { client.acorn.createAcornMessage(messageId, channel) }
}
