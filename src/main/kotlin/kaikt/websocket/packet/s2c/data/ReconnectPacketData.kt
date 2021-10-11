package kaikt.websocket.packet.s2c.data

import com.google.gson.annotations.SerializedName

data class ReconnectPacketData(
	val code: Int,
	@SerializedName("err")
	val error: String
)