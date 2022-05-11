package kaikt.websocket.yeti.sender.guild

import kaikt.api.entity.definition.KGuildViewDefinition
import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.response.KMessageCreateData
import kaikt.api.entity.response.KResponse
import kaikt.websocket.yeti.YetiBot
import kaikt.websocket.yeti.sender.Sender

class Guild(private val bot: YetiBot, val id: String) {

	private var viewCache: KGuildViewDefinition? = null

	val view: KGuildViewDefinition
		get() {
			if(viewCache == null)
				renewGuildView()
			return viewCache!!
		}

	/**
	 * 重新获取服务器信息
	 */
	fun renewGuildView() {
		viewCache = bot.kApi.Guild().getGuildView(id).throwIfNotSuccess().data
	}
}