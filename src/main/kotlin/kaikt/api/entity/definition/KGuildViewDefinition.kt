package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KChannelDefinition
import kaikt.api.entity.definition.KRoleDefinition

data class KGuildViewDefinition(
	val id: String,
	val name: String,
	val topic: String,
	@SerializedName("master_id")
	val masterId: String,
	val icon: String,
	@SerializedName("notify_type")
	val notifyType: Int,
	val region: String,
	@SerializedName("enable_open")
	val enableOpen: Boolean,
	@SerializedName("open_id")
	val openId: String,
	@SerializedName("default_channel_id")
	val defaultChannelId: String,
	@SerializedName("welcome_channel_id")
	val welcomeChannelId: String,
	val features: List<Any>, // TODO: ??
	@SerializedName("boost_num")
	val boostNumber: Int,
	val level: Int,
	val roles: List<KRoleDefinition>,
	val channels: List<KChannelDefinition>,
	@SerializedName("user_config")
	val userConfig: KUserConfigDefinition
)
