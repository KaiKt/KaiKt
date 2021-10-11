package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class PinMessageBody(
	@SerializedName("channel_id")
	val channelId: String,
	@SerializedName("operator_id")
	val operatorId: String,
	@SerializedName("msg_id")
	val msgId: String
)
