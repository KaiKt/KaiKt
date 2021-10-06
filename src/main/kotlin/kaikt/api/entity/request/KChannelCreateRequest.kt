package kaikt.api.entity.request

import kaikt.api.entity.enum.KChannelType
import kaikt.api.util.valueNotNullMapOf

class KChannelCreateRequest(val guildId: String, val name: String) {

	/**
	 * 父分组ID
	 */
	var parentId: String? = null

	/**
	 * 频道类型
	 */
	var type: Int? = null

	/**
	 * 语音人数限制
	 */
	var limitAmount: Int? = null

	/**
	 * 语音质量；1-流畅，2-正常，3-高质量
	 */
	var voiceQuality: Int? = null

	val body: Map<String, Any?>
		get() = valueNotNullMapOf(
			"guild_id" to guildId,
			"name" to name,
			"parent_id" to parentId,
			"type" to type,
			"limit_amount" to limitAmount,
			"voice_quality" to voiceQuality
		)

	fun setType(type: KChannelType) = apply { this.type = type.type }
}