package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KUserDefinition

data class KGuildEmojiCreateData(
	val name: String,
	val id: String,
	@SerializedName("emoji_type")
	val emojiType: Int,

	/**
	 * 表情创建者信息
	 */
	@SerializedName("user_info")
	val userInfo: KUserDefinition
)