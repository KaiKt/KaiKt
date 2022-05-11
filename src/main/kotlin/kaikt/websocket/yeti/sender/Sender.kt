package kaikt.websocket.yeti.sender

import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.response.KMessageCreateData
import kaikt.api.entity.response.KResponse

interface Sender {

	fun sendMessage(
		content: String,
		type: KMessageType? = null,
		quote: String? = null,
		nonce: String? = null
	): KResponse<KMessageCreateData>

	fun sendCardMessage(cardContent: String, quote: String? = null, nonce: String? = null) =
		sendMessage(cardContent, KMessageType.CardMessage, quote, nonce)

}