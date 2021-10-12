package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KEmojiDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.direct.HPrivateMessage
import kaikt.websocket.hazelnut.direct.HUserChat

data class PrivateDeletedReactionEvent(
	val client: KaiClient,

	// Extra
	val chatCode: String,
	val emoji: KEmojiDefinition,
	val userId: String,
	val messageId: String
) {

	val sender get() = HUser(client.api, userId)

	val chat get() = HUserChat(client.api, chatCode, client.api.meUser, null)

	val message get() = HPrivateMessage(client.api, 255, chat, messageId, null, null)

}
