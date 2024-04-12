package kaikt.websocket

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kaikt.gson
import kaikt.websocket.packet.Packet
import kaikt.websocket.packet.Sig

internal data class PeekPacketResult(
	val valid: Boolean,
	private val type: Int?,
	private val data: JsonElement?,
	private val exception: Exception?,
) {

	/**
	 * 把 JSON 格式的文本数据转换成需要的 [T] 类型实例。
	 *
	 * @throws IllegalStateException 当数据异常时
	 */
	inline fun <reified T : Packet> getAs(): T =
		data?.let { gson.fromJson(it, T::class.java) } ?: throw IllegalStateException("无效数据")

	fun isType(sig: Sig) =
		type != null && type == sig.value

	fun getType(): Sig {
		if(type == null) error("无效消息")
		return Sig.get(type) ?: error("无效信令 $type")
	}

	fun getData(): JsonElement {
		if(data == null) error("无效消息")
		return data
	}

	fun getException(): Exception {
		return exception ?: error("消息无异常")
	}

}

internal fun peekPacket(message: String): PeekPacketResult {
	try {
		val json = JsonParser.parseString(message)
		try {
			val type = json.asJsonObject.getAsJsonPrimitive("s").asInt
			return PeekPacketResult(true, type, json, null)
		} catch(ex: Exception) {
			return PeekPacketResult(false, null, json, ex)
		}
	} catch(ex: Exception) {
		return PeekPacketResult(false, null, null, ex)
	}
}