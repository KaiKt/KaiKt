package kaikt.websocket.hazelnut.direct

import kaikt.api.KaiApi
import kaikt.api.entity.request.TargetIdOrChatCode
import kaikt.websocket.hazelnut.HUser

data class HUserChat(
	private val api: KaiApi,

	val chatCode: String,
	val me: HUser,

	val you: HUser?
) {

	fun sendMessage(content: String): HPrivateMessage {
		val create = api.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withChatCode(chatCode), content)
		return HPrivateMessage(api, 1, this, create.data.msgId, content, me)
	}

}
