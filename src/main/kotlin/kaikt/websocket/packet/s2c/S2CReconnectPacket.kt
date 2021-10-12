package kaikt.websocket.packet.s2c

import com.google.gson.annotations.SerializedName
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.s2c.data.ReconnectPacketData

data class S2CReconnectPacket(
	val s: Int,
	@SerializedName("d")
	val data: ReconnectPacketData
): Packet
