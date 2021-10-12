package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class UpdatedMessageBody(
	@SerializedName("channel_id")
	val channelId: String,
	val content: String,
	val mention: List<String>,
	@SerializedName("mention_all")
	val mentionAll: Boolean,
	@SerializedName("mention_here")
	val mentionHere: Boolean,
	@SerializedName("mention_roles")
	val mentionRoles: List<String>,
	@SerializedName("updated_at")
	val updatedAt: Long,
	@SerializedName("msg_id")
	val msgId: String
)
