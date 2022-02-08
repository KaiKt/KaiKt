package kaikt.websocket.yeti.sender.guild

import kaikt.websocket.yeti.YetiBot

class GuildManager(private val bot: YetiBot) {

	private val guildCache = mutableMapOf<String, Guild>()

	operator fun get(id: String): Guild {
		if(id !in guildCache) {
			guildCache[id] = Guild(bot, id)
		}
		return guildCache[id]!!
	}

}