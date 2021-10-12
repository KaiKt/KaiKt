package kaikt.websocket.packet.s2c

import com.google.gson.annotations.SerializedName
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.s2c.data.EventPacketData

data class S2CEventPacket(
	val s: Int,
	@SerializedName("d")
	val data: EventPacketData,
	val sn: Int
): Packet