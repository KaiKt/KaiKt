package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KChannelDefinition
import kaikt.websocket.KaiClient

data class GuildUpdatedChannelEvent(
	val client: KaiClient,

	val guildId: String,
	val channelDefinition: KChannelDefinition
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
	val channel by lazy { client.acorn.buildAcornChannel {
		this.channelId = channelDefinition.id
		this.guildId = channelDefinition.guildId
	} }
}
