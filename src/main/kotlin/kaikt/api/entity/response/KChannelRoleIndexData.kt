package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KPermissionUserDefinition
import kaikt.api.entity.definition.KPermissionOverwritesDefinition

data class KChannelRoleIndexData(
	@SerializedName("permission_overwrites")
	val permissionOverwrites: List<KPermissionOverwritesDefinition>,
	@SerializedName("permission_users")
	val permissionUsers: List<KPermissionUserDefinition>,
	@SerializedName("permission_sync")
	val permissionSync: Int
)