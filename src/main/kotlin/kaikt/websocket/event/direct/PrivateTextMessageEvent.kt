package kaikt.websocket.event.direct

import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.KaiClient

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
)