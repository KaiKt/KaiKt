package kaikt.websocket.acorn.entity

import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.request.*
import kaikt.websocket.acorn.*

/**
 * 发送消息的数据
 */
class AcornMessageRequest(val source: AcornMessageSource, val content: String) {

	var type: KMessageType = KMessageType.Text
	var quote: String? = null
	var nonce: String? = null
	var tempTargetId: String? = null

	fun writeData(request: Any) {
		when(request) {
			is KMessageCreateRequest -> {
				request.type = type
				request.quote = quote
				request.nonce = nonce
				request.tempTargetId = tempTargetId
			}
			is KDirectMessageCreateRequest -> {
				request.type = type
				request.quote = quote
				request.nonce = nonce
			}
			else -> {
				throw IllegalStateException("Cannot write data to ${request.javaClass}.")
			}
		}
	}

}