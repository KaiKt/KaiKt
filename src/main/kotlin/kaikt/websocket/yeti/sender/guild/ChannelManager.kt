package kaikt.websocket.yeti.sender.guild

import kaikt.websocket.yeti.YetiBot

class ChannelManager(private val bot: YetiBot) {

	private val channelCache = mutableMapOf<String, Channel>()

	operator fun get(id: String): Channel {
		if(id !in channelCache) {
			channelCache[id] = Channel(bot, id)
		}
		return channelCache[id]!!
	}
}