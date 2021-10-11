package kaikt.websocket.hazelnut.guild

import kaikt.api.KaiApi
import kaikt.api.entity.definition.KRoleDefinition

data class HRole(
	private val api: KaiApi,
	val roleId: String,
	val permissions: Int
)

fun KRoleDefinition.toHRole(api: KaiApi) = HRole(api, roleId, permissions)