package kaikt.websocket.yeti.sender.guild

import kaikt.api.entity.definition.KChannelViewDefinition
import kaikt.api.entity.enum.KMessageType
import kaikt.websocket.yeti.YetiBot
import kaikt.websocket.yeti.sender.Sender

class Channel(private val bot: YetiBot, val id: String) : Sender {

	private var viewCache: KChannelViewDefinition? = null

	val view: KChannelViewDefinition
		get() {
			if(viewCache == null)
				renewChannelView()
			return viewCache!!
		}

	fun renewChannelView() {
		viewCache = bot.kApi.Channel().getChannelView(id).throwIfNotSuccess().data
	}

	override fun sendMessage(content: String, type: KMessageType?, quote: String?, nonce: String?) =
		bot.kApi.Message().postMessageCreate(view.id, content) {
			this.quote = quote
			this.nonce = nonce
		}
}