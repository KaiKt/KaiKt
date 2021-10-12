package kaikt.websocket.packet.s2c

import com.google.gson.annotations.SerializedName
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.s2c.data.ResumeAckPacketData

data class S2CResumeAckPacket(
	val s: Int,
	@SerializedName("data")
	val data: ResumeAckPacketData
): Packet