package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KUserDefinition

data class KBlacklistData(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("created_time")
	val createdTime: Int,
	val remark: String,
	val user: KUserDefinition
)
