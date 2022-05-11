package kaikt.websocket.yeti.sender.user

import kaikt.websocket.yeti.YetiBot

class UserManager(private val bot: YetiBot) {

	private val userCache = mutableMapOf<String, User>()

	operator fun get(id: String): User {
		if(id !in userCache) {
			userCache[id] = User(bot, id)
		}
		return userCache[id]!!
	}

}