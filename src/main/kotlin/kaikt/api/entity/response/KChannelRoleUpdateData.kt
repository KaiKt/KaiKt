package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.*

data class KChannelRoleUpdateData(
	val user: KUserDefinition?,
	@SerializedName("role_id")
	val roleId: String?,
	val allow: Int,
	val deny: Int
) {

	val isPermissionUser get() = user != null

	val asPermissionUser
		get() = if(user != null) KPermissionUserDefinition(user, allow, deny) else throw NoSuchElementException("user")

	val isPermissionRole get() = roleId != null

	val asPermissionRole
		get() = if(roleId != null) KPermissionRoleDefinition(roleId, allow, deny) else throw NoSuchElementException("role_id")

}