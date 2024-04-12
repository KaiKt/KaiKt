package kaikt.websocket.yeti

import kaikt.api.KToken
import kaikt.api.KaiApi
import kaikt.api.entity.definition.KUserDefinition
import kaikt.websocket.EventBusKaiClient
import kaikt.websocket.yeti.command.CommandManager
import kaikt.websocket.yeti.sender.guild.ChannelManager
import kaikt.websocket.yeti.sender.guild.GuildManager
import kaikt.websocket.yeti.sender.user.UserManager
import org.slf4j.LoggerFactory

class YetiBot(config: YetiBotConfig) {

	constructor(configureBlock: YetiBotConfig.Builder.() -> Unit) : this(YetiBotConfig.Builder().apply(configureBlock).build())

	private val logger = LoggerFactory.getLogger(config.loggerName)

	val kApi: KaiApi
	val kCli: EventBusKaiClient

	internal val userManager: UserManager
	internal val guildManager: GuildManager
	internal val channelManager: ChannelManager

	internal val commandManager: CommandManager

	internal val selfView: KUserDefinition

	init {
		logger.info("Yeti")
		YetiEventExt.setSpecifiedYetiBot(this)

		logger.info("正在配置 API 令牌")
		val token = KToken(KToken.TokenType.Bot, config.token)
		logger.debug("API 令牌为 '${token.toFullToken()}'")
		this.kApi = token.toApi()

		logger.info("正在配置 WS 客户端")
		this.kCli = EventBusKaiClient(kApi)

		logger.info("正在配置 InstManagers")
		this.userManager = UserManager(this)
		this.guildManager = GuildManager(this)
		this.channelManager = ChannelManager(this)

		logger.info("正在读取信息")
		this.selfView = kCli.me
		logger.info("机器人 ${selfView.username}(${selfView.id})")

		logger.info("正在配置命令管理器")
		this.commandManager = CommandManager(this)

		logger.info("正在启动客户端监听")
		this.kCli.initialize()
	}

}