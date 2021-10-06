package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KUserDefinition(
	val id: String,
	val username: String,
	val nickname: String,
	@SerializedName("identify_num")
	val identifyNum: String,
	val online: Boolean,
	val bot: Boolean,
	val status: Int,
	val avatar: String,
	@SerializedName("vip_avatar")
	val vipAvatar: String,
	@SerializedName("mobile_verified")
	val mobileVerified: Boolean,
	@SerializedName("joined_at")
	val joinedAt: Long,
	@SerializedName("active_time")
	val activeTime: Long,
	val roles: List<Number>,
	@SerializedName("is_master")
	val isMaster: Boolean,
	val abbr: String?,
	val color: Int,
	val os: String,
	@SerializedName("tag_info")
	val tagInfo: KUserTagInfoDefinition?
)
