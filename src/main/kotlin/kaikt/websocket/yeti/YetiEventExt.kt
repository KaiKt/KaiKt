package kaikt.websocket.yeti

private lateinit var specifiedYetiBot: YetiBot

object YetiEventExt {
	/**
	 * 定义默认机器人实例
	 * 在 [YetiBot] 构造函数中调用
	 */
	fun setSpecifiedYetiBot(yetiBot: YetiBot) {
		specifiedYetiBot = yetiBot
	}
}

fun String.getUser(bot: YetiBot = specifiedYetiBot) = bot.userManager[this]
fun String.getGuild(bot: YetiBot = specifiedYetiBot) = bot.guildManager[this]
fun String.getChannel(bot: YetiBot = specifiedYetiBot) = bot.channelManager[this]