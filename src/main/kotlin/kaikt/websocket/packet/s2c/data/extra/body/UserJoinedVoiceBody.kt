package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class UserJoinedVoiceBody(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("channel_id")
	val channelId: String,
	@SerializedName("joined_at")
	val joinedAt: Long
)
