package kaikt.websocket.packet.s2c

import com.google.gson.annotations.SerializedName
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.s2c.data.HelloPacketData

data class S2CHelloPacket(
	val s: Int,
	@SerializedName("d")
	val data: HelloPacketData
): Packet
