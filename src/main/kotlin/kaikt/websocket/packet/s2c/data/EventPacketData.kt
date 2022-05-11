package kaikt.websocket.packet.s2c.data

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import kaikt.api.util.IntBool
import kaikt.api.util.IntBoolTypeAdapter
import kaikt.gson
import kaikt.websocket.packet.s2c.data.extra.NonSystemExtra
import kaikt.websocket.packet.s2c.data.extra.SystemExtra

typealias EventPacketDataExtra = JsonObject

data class EventPacketData(
	@SerializedName("channel_type")
	val channelType: ChannelType,
	val type: Int,
	@SerializedName("target_id")
	val targetId: String,
	@SerializedName("author_id")
	val authorId: String,
	val content: String,
	@SerializedName("msg_id")
	val msgId: String,
	@SerializedName("msg_timestamp")
	val msgTimestamp: Long,
	val nonce: String,
	val extra: EventPacketDataExtra
) {

	val isSystemType get() = type == 255

	val nonSystemExtra: NonSystemExtra
		get() =
			if(!isSystemType) {
				gson.fromJson(extra, NonSystemExtra::class.java)
			} else {
				throw IllegalStateException("type == 255")
			}

	val systemExtra: SystemExtra
		get() =
			if(isSystemType) {
				gson.fromJson(extra, SystemExtra::class.java)
			} else {
				throw IllegalStateException("type($type) != 255")
			}
}



