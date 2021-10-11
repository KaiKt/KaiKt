package kaikt.websocket.hazelnut.guild

import kaikt.api.KaiApi
import kaikt.api.entity.enum.KMessageType
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.toHUser

data class HGuildMessage(
	private val api: KaiApi,
	val type: Int = 1,
	val channel: HChannel,
	val messageId: String,

	val content: String?,
	val author: HUser?
) {

	fun modify(content: String): HGuildMessage {
		api.Message().postMessageUpdate(messageId, content)
		return HGuildMessage(api, 1, channel, messageId, content, author)
	}

	fun reply(content: String, type: KMessageType? = null): HGuildMessage {
		val create = api.Message().postMessageCreate(channel.channelId, content) {
			this.quote = messageId
			this.type = type
		}
		return HGuildMessage(api, type?.i ?: 1, channel, create.msgId, content, api.me.toHUser(api, channel.guild))
	}

	fun delete() {
		api.Message().postMessageDelete(messageId)
	}

	fun getReactions(emoji: String) = api.Message().getMessageReactionList(messageId, emoji)
	fun createReaction(emoji: String) = api.Message().postMessageAddReaction(messageId, emoji)
	fun deleteReaction(emoji: String) = api.Message().postMessageDeleteReaction(messageId, emoji)

}
