package kaikt.websocket.packet.s2c.data

import com.google.gson.annotations.SerializedName

data class HelloPacketData(
	val code: Int,
	@SerializedName("session_id")
	val sessionId: String
)