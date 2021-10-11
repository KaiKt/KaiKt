package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class UpdatedPrivateMessageBody(
	@SerializedName("author_id")
	val authorId: String,
	@SerializedName("target_id")
	val targetId: String,
	@SerializedName("msg_id")
	val msgId: String,
	val content: String,
	@SerializedName("updated_at")
	val updatedAt: Long,
	@SerializedName("chat_code")
	val chatCode: String
)