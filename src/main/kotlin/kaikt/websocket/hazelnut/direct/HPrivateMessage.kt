package kaikt.websocket.hazelnut.direct

import kaikt.api.KaiApi
import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.request.TargetIdOrChatCode
import kaikt.websocket.hazelnut.HUser

data class HPrivateMessage(
	private val api: KaiApi,
	val type: Int,
	val chat: HUserChat,
	val messageId: String,
	val content: String?,
	val author: HUser?
) {

	fun modify(content: String): HPrivateMessage {
		api.DirectMessage().postDirectMessageUpdate(messageId, content)
		return HPrivateMessage(api, type, chat, messageId, content, author)
	}

	fun reply(content: String, type: KMessageType? = null): HPrivateMessage {
		val create = api.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withChatCode(chat.chatCode), content) {
			this.quote = messageId
			this.type = type
		}
		return HPrivateMessage(api, type?.i ?: 1, chat, create.msgId, content, chat.me)
	}

	fun delete() {
		api.DirectMessage().postDirectMessageDelete(messageId)
	}

	fun getReactions(emoji: String) = api.DirectMessage().getDirectMessageReactionList(messageId, emoji)
	fun createReaction(emoji: String) = api.DirectMessage().postDirectMessageAddReaction(messageId, emoji)
	fun deleteReaction(emoji: String) = api.DirectMessage().postDirectMessageDeleteReaction(messageId, emoji)

}
