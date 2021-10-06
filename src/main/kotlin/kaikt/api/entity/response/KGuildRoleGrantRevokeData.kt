package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName

data class KGuildRoleGrantRevokeData(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("guild_id")
	val guildId: String,

	/**
	 * 修改后用户的身分组ID列表
	 */
	val roles: List<String>
)