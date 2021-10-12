package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KEmojiDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.guild.HChannel
import kaikt.websocket.hazelnut.guild.HGuildMessage

data class GuildDeletedReactionEvent(
	val client: KaiClient,

	// Extra
	val channelId: String,
	val emoji: KEmojiDefinition,
	val userId: String,
	val messageId: String
) {

	val sender get() = HUser(client.api, userId)

	val channel get() = HChannel(client.api, null, channelId)

	val message get() = HGuildMessage(client.api, 255, channel, messageId, null, sender)

}
