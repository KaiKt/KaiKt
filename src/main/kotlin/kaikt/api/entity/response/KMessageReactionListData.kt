package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName

data class KMessageReactionListData(
	val id: String,
	val username: String,
	@SerializedName("identify_num")
	val identifyNum: String,
	val online: Boolean,
	val status: Int,
	val avatar: String,
	val bot: Boolean,
	@SerializedName("tag_info")
	val tagInfo: Any,
	val nickname: String,
	@SerializedName("reaction_time")
	val reactionTime: Long
)
