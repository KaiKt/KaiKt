package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class UpdatedGuildMemberBody(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("nickname")
	val nickname: String
)