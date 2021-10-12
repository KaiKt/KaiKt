package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class UserUpdatedBody(
	@SerializedName("user_id")
	val userId: String,
	val username: String,
	val avatar: String
)
