package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KPermissionOverwritesDefinition(@SerializedName("role_id") val roleId: Long, val allow: Long, val deny: Long)
