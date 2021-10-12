package kaikt.websocket.event.direct

import kaikt.api.entity.definition.*
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.*
import kaikt.websocket.hazelnut.direct.HPrivateMessage
import kaikt.websocket.hazelnut.direct.HUserChat

data class PrivateKMarkdownMessageEvent(
	val client: KaiClient,

	val imageUrl: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition,
	val kMarkdown: KMarkdownDefinition
) {

	val sender get() = author.toHUser(client.api)

	val chat get() = HUserChat(client.api, chatCode, client.api.meUser, sender)

	val message get() = HPrivateMessage(client.api, 1, chat, messageId, imageUrl, sender)
}