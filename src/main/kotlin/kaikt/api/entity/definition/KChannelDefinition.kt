package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KChannelDefinition(
	val id: String,
	@SerializedName("guild_id")
	val guildId: String,
	@SerializedName("master_id")
	val masterId: String,
	@SerializedName("parent_id")
	val parentId: String,
	val name: String,
	val topic: String,
	val type: Int,
	val level: Int,
	@SerializedName("slow_mode")
	val slowMode: Int,
	@SerializedName("limit_amount")
	val limitAmount: Int,
	@SerializedName("is_category")
	val isCategory: Boolean,
	@SerializedName("permission_sync")
	val permissionSync: Int,
	@SerializedName("permission_overwrites")
	val permissionOverwrites: List<KPermissionOverwritesDefinition>,
	@SerializedName("permission_users")
	val permissionUsers: List<KPermissionUserDefinition>,
	@SerializedName("voice_quality")
	val voiceQuality: Int,
	@SerializedName("server_url")
	val serverUrl: String
)