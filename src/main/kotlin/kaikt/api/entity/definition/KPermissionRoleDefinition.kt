package kaikt.api.entity.definition

import kaikt.api.entity.response.KChannelRoleUpdateData

/**
 * @see KChannelRoleUpdateData
 */
data class KPermissionRoleDefinition(
	val roleId: String,
	val allow: Int,
	val deny: Int
)
