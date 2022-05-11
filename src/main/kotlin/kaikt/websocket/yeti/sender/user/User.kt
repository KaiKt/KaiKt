package kaikt.websocket.yeti.sender.user

import kaikt.api.entity.definition.KUserDefinition
import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.request.TargetIdOrChatCode
import kaikt.websocket.yeti.YetiBot
import kaikt.websocket.yeti.sender.Sender

class User(private val bot: YetiBot, val id: String) : Sender {

	private var definitionCache: KUserDefinition? = null

	val definition: KUserDefinition
		get() {
			if(definitionCache == null)
				renewDefinition()
			return definitionCache!!
		}

	fun renewDefinition(inGuild: String? = null) {
		definitionCache = bot.kApi.User().getUserView(id, inGuild).throwIfNotSuccess().data
	}

	override fun sendMessage(content: String, type: KMessageType?, quote: String?, nonce: String?) =
		bot.kApi.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withTargetId(definition.id), content) {
			this.type = type
			this.quote = quote
			this.nonce = nonce
		}

}