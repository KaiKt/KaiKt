package kaikt.websocket.packet.c2s

import kaikt.websocket.packet.Packet

class C2SPingPacket(val sn: Int) : Packet {
	val s: Int = 2
}