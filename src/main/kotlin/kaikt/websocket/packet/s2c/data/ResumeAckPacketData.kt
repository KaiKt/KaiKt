package kaikt.websocket.packet.s2c.data

import com.google.gson.annotations.SerializedName

data class ResumeAckPacketData(
	@SerializedName("session_id")
	val sessionId: String
)