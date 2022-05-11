package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient
import kaikt.websocket.hazelnut.*
import kaikt.websocket.hazelnut.direct.HPrivateMessage
import kaikt.websocket.hazelnut.direct.HUserChat

@Deprecated("开黑啦已经移除 Text 消息，并全部转为 KMarkdown 消息", replaceWith = ReplaceWith("PrivateKMarkdownMessageEvent"))
data class PrivateTextMessageEvent(
	val client: KaiClient,

	val content: String,
	val authorId: String,
	val targetId: String,
	val messageId: String,
	val messageTimestamp: Long,
	// Extra
	val chatCode: String,
	val author: KUserDefinition
) {

	val sender get() = author.toHUser(client.api)

	val chat get() = HUserChat(client.api, chatCode, client.api.meUser, sender)

	val message get() = HPrivateMessage(client.api, 1, chat, messageId, content, sender)
}