package kaikt.api.entity.request

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KUserDefinition

data class KGuildEmojiDefinition(
	val name: String,
	val id: String,
	@SerializedName("user_info")
	val userInfo: KUserDefinition
)
