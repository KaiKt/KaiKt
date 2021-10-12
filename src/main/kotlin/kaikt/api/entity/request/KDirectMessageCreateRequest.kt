package kaikt.api.entity.request

import kaikt.api.entity.enum.KMessageType
import kaikt.api.util.valueNotNullMapOf

data class KDirectMessageCreateRequest(val targetIdOrChatCode: TargetIdOrChatCode, val content: String) {

	var type: KMessageType? = null
	var quote: String? = null
	var nonce: String? = null

	val body
		get() = valueNotNullMapOf(
			"type" to type?.i,
			"target_id" to targetIdOrChatCode.targetId,
			"chat_code" to targetIdOrChatCode.chatCode,
			"content" to content,
			"quote" to quote,
			"nonce" to nonce
		)

}
