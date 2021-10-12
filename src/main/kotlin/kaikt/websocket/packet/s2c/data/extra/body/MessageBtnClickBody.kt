package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class MessageBtnClickBody(
	val value: String,
	@SerializedName("msg_id")
	val msgId: String,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("target_id")
	val targetId: String
)
