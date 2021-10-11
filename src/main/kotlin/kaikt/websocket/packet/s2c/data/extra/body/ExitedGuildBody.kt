package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class ExitedGuildBody(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("exited_at")
	val exitedAt: Long
)