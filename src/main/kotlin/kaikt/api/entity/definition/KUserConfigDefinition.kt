package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KUserConfigDefinition(
	@SerializedName("notify_type")
	val notifyType: Int?,
	val nickname: String,
	@SerializedName("role_ids")
	val roleIds: List<String>,
	@SerializedName("chat_setting")
	val chatSetting: Int,
	@SerializedName("security_limit")
	val securityLimit: Any?
)
