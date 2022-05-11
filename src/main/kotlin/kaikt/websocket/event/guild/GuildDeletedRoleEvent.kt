package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KRoleDefinition
import kaikt.websocket.KaiClient

data class GuildDeletedRoleEvent(
	val client: KaiClient,

	val guildId: String,
	val roleDefinition: KRoleDefinition
)