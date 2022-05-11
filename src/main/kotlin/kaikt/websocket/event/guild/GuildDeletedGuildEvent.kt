package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KGuildDefinition
import kaikt.websocket.KaiClient

data class GuildDeletedGuildEvent(
	val client: KaiClient,

	val guildId: String,
	val guildDefinition: KGuildDefinition
)