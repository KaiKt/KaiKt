package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class JoinedGuildBody(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("joined_at")
	val joinedAt: Long
)