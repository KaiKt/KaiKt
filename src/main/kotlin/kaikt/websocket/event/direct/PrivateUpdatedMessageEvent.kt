package kaikt.websocket.event.direct

import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.direct.HPrivateMessage
import kaikt.websocket.hazelnut.direct.HUserChat

data class PrivateUpdatedMessageEvent(
	val client: KaiClient,

	val content: String,
	val authorId: String,
	val messageId: String,
	val updatedTimestamp: Long,
	// Extra
	val chatCode: String
) {

	val chat get() = HUserChat(client.api, chatCode, client.api.meUser, HUser(client.api, authorId))

	val updatedMessage get() = HPrivateMessage(client.api, 1, chat, messageId, content, null)
}