package kaikt.websocket.event.direct

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.direct.HPrivateMessage
import kaikt.websocket.hazelnut.direct.HUserChat

data class PrivateDeletedMessageEvent(
	val client: KaiClient,

	val authorId: String,
	val messageId: String,
	val deletedTimestamp: Long,
	// Extra
	val chatCode: String
) {

	val chat get() = HUserChat(client.api, chatCode, client.api.meUser, HUser(client.api, authorId))

	val deletedMessage get() = HPrivateMessage(client.api, 1, chat, messageId, null, null)
}