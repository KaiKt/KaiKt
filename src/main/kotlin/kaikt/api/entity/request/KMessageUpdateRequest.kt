package kaikt.api.entity.request

import kaikt.api.util.valueNotNullMapOf

class KMessageUpdateRequest(val msgId: String, val content: String) {

	/**
	 * 回复/引用
	 * @see KMessageCreateRequest.quote
	 */
	var quote: String? = null

	/**
	 * 用户ID
	 * @see KMessageCreateRequest.tempTargetId
	 */
	var tempTargetId: String? = null

	val body
		get() = valueNotNullMapOf(
			"msg_id" to msgId,
			"content" to content,
			"quote" to quote,
			"temp_target_id" to tempTargetId
		)

}