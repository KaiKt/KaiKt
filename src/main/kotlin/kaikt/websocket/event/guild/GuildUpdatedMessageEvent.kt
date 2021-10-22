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
) {

	val guild by lazy { client.acorn.createAcornGuild(guildId) }

	val channel by lazy { client.acorn.createAcornChannel(channelId) }

	val updatedMessage by lazy { client.acorn.buildAcornMessage {
		this.messageId = this@GuildUpdatedMessageEvent.messageId
		this.source = channel
		this.messageContent = updatedContent
		this.messageTimestamp = updatedAt
	} }

}