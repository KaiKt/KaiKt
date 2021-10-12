package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class DeletedMessageBody(
	@SerializedName("channel_id")
	val channelId: String,
	@SerializedName("msg_id")
	val msgId: String
)
