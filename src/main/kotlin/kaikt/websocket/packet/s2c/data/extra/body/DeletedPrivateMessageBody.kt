package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class DeletedPrivateMessageBody(
	@SerializedName("author_id")
	val authorId: String,
	@SerializedName("target_id")
	val targetId: String,
	@SerializedName("msg_id")
	val msgId: String,
	@SerializedName("deleted_at")
	val deletedAt: Long,
	@SerializedName("chat_code")
	val chatCode: String
)