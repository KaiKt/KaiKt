package kaikt.websocket.yeti.command

import kaikt.websocket.yeti.YetiBot

class CommandManager(private val bot: YetiBot) {

	var listener: YetiCommandListener? = null

	fun enableListener(listener: YetiCommandListener) {
		this.listener = listener
		bot.kCli.eventBus.register(this.listener)
	}

	fun disableListener() {
		bot.kCli.eventBus.unregister(this.listener)
	}

}