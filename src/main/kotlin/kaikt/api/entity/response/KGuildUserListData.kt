package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KUserDefinition

data class KGuildUserListData(
	@SerializedName("user_count")
	val userCount: Int,
	@SerializedName("online_count")
	val onlineCount: Int,
	@SerializedName("offline_count")
	val OfflineCount: Int,
	val items: List<KUserDefinition>
)