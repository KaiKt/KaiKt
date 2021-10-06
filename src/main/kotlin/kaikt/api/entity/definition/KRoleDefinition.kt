package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KRoleDefinition(
	@SerializedName("role_id")
	val roleId: String,
	val name: String,
	val color: Int,
	val position: Int,
	val hoist: Int,
	val mentionable: Int,
	val permissions: Int,
	val type: Int
)