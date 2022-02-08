package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KChannelDefinition
import kaikt.websocket.KaiClient

data class GuildAddedChannelEvent(
	val client: KaiClient,

	val guildId: String,
	val channelDefinition: KChannelDefinition
)