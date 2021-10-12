package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KGuildDefinition(
	val id: String,
	val name: String,
	val topic: String,
	@SerializedName("master_id")
	val masterId: String,
	@SerializedName("is_master")
	val isMaster: Boolean,
	val icon: String,
	@SerializedName("notify_type")
	val notifyType: Int,
	val region: String,
	@SerializedName("default_channel_id")
	val defaultChannelId: String,
	@SerializedName("welcome_channel_id")
	val welcomeChannelId: String
)