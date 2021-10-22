package kaikt.websocket.event.guild

import kaikt.api.entity.definition.KRoleDefinition
import kaikt.websocket.KaiClient

data class GuildAddedRoleEvent(
	val client: KaiClient,

	val guildId: String,
	val roleDefinition: KRoleDefinition
) {
	val guild by lazy { client.acorn.createAcornGuild(guildId) }
}
