package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KEmojiDefinition

data class PrivateReactionBody(
	@SerializedName("chat_code")
	val chatCode: String,
	val emoji: KEmojiDefinition,
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("msg_id")
	val msgId: String
)
