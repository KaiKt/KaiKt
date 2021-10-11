package kaikt

import kaikt.api.KToken
import kaikt.websocket.KaiClient
import kaikt.websocket.event.direct.*
import kaikt.websocket.event.guild.*
import kaikt.websocket.hazelnut.guild.firstTextChannel
import org.apache.logging.log4j.LogManager
import org.greenrobot.eventbus.Subscribe

class TestClient {

	private val log = LogManager.getLogger("KaiClient")
	private val api = KToken(KToken.TokenType.Bot, "1/MTA0MjE=/zN5Lh9Na4wZPk0fpeTjDIg==").toApi()

	fun start() {
		val cli = KaiClient(api)

		cli.eventBus.register(this)

		cli.start()
	}

	@Subscribe
	fun guildTextMessage(event: GuildTextMessageEvent) {
		log.info(event)
		event.message.reply("哦哦，知道了")

//		event.message.content?.startsWith("https://www.bilibili.com/video/").let {
//			val bv = event.message.content?.substring("https://www.bilibili.com/video/".length)
//			event.message.reply(AvBv.b2v(bv!!))
//		}
	}

	@Subscribe
	fun privateTextMessage(event: PrivateTextMessageEvent) {
		log.info(event)
		event.message.reply("哦哦，好的好的")

		event.sender.chat.sendMessage("你什么意思？").createReaction("[#129300;]")
	}

	@Subscribe
	fun guildImageMessage(event: GuildImageMessageEvent) {
		log.info(event)
		event.message.reply(event.attachments.url)
	}

	@Subscribe
	fun privateImageMessage(event: PrivateImageMessageEvent) {
		log.info(event)
		event.message.reply(event.attachments.url)
	}

	@Subscribe
	fun guildVideoMessage(event: GuildVideoMessageEvent) {
		log.info(event)
		event.message.reply(event.attachments.url)
	}

	@Subscribe
	fun privateVideoMessage(event: PrivateVideoMessageEvent) {
		log.info(event)
		event.message.reply(event.attachments.url)
	}

	@Subscribe
	fun guildKMarkdownMessage(event: GuildKMarkdownMessageEvent) {
		log.info(event)
		event.message.reply(event.kMarkdown.rawContent)
	}

	@Subscribe
	fun privateKMarkdownMessage(event: PrivateKMarkdownMessageEvent) {
		log.info(event)
		event.message.reply(event.kMarkdown.rawContent)
	}

	@Subscribe
	fun guildCardMessage(event: GuildCardMessageEvent) {
		log.info(event)
		event.message.reply(event.kMarkdown.rawContent)
	}

	@Subscribe
	fun privateCardMessage(event: PrivateCardMessageEvent) {
		log.info(event)
		event.message.reply(event.kMarkdown.rawContent)
	}

	@Subscribe
	fun guildAddedReaction(event: GuildAddedReactionEvent) {
		log.info(event)
		event.message.reply("${event.userId == api.meUser.userId}")
		event.message.createReaction(event.emoji.id)
	}

	@Subscribe
	fun guildDeletedMessage(event: GuildDeletedMessageEvent) {
		log.info(event)
		event.channel.sendMessage("Deleted Message: ${event.messageId}")
	}

	@Subscribe
	fun guildAddedChannel(event: GuildAddedChannelEvent) {
		log.info(event)
		event.channel.sendMessage("哟，新频道哦！")
	}

	@Subscribe
	fun guildUpdatedChannel(event: GuildUpdatedChannelEvent) {
		log.info(event)
		event.channel.sendMessage("额，频道被更新了欸！")
	}

	@Subscribe
	fun guildDeletedChannel(event: GuildDeletedChannelEvent) {
		log.info(event)
		event.guild.channels.first { it.isCategory != true }.sendMessage("额，频道被删了欸！${event.channelId}")
	}

	@Subscribe
	fun guildPinnedMessage(event: GuildPinnedMessageEvent) {
		log.info(event)
		event.message.reply("芜湖！")
	}

	@Subscribe
	fun guildUnpinnedMessage(event: GuildUnpinnedMessageEvent) {
		log.info(event)
		event.message.reply("芜湖？Unpinned")
	}

	@Subscribe
	fun privateUpdateMessage(event: PrivateUpdatedMessageEvent) {
		log.info(event)
		event.updatedMessage.reply(event.chatCode)
	}

	@Subscribe
	fun privateDeleteMessage(event: PrivateDeletedMessageEvent) {
		log.info(event)
		event.chat.sendMessage(event.deletedMessage.messageId)
	}

	@Subscribe
	fun privateAddReaction(event: PrivateAddedReactionEvent) {
		log.info(event)
		event.message.createReaction(event.emoji.id)
	}

	@Subscribe
	fun privateDeleteReaction(event: PrivateDeletedReactionEvent) {
		log.info(event)
		event.message.deleteReaction(event.emoji.id)
	}

	@Subscribe
	fun guildUserJoined(event: GuildUserJoinedEvent) {
		log.info(event)
		event.guild.channels.first { it.isCategory != true }.sendMessage("欢迎 ${event.user.kView.nickname}")
	}

	@Subscribe
	fun guildUserExited(event: GuildUserExitedEvent) {
		log.info(event)
		event.guild.channels.first { it.isCategory != true }.sendMessage("再见 ${event.user.kView.username}")
	}

	@Subscribe
	fun guildUserUpdated(event: GuildUserUpdatedEvent) {
		log.info(event)
		event.guild.channels.first { it.isCategory != true }.sendMessage("${event.nickname} 改名了！")
	}

	@Subscribe
	fun guildMemberOnline(event: GuildMemberOnlineEvent) {
		log.info(event)
		event.guild.channels.first { it.isCategory != true }.sendMessage("${event.user.nickname} 上线了！")
	}

	@Subscribe
	fun guildMemberOffline(event: GuildMemberOfflineEvent) {
		log.info(event)
		event.guild.channels.first { it.isCategory != true }.sendMessage("${event.user.nickname} 下线了！")
	}

	@Subscribe
	fun guildAddRole(e: GuildAddedRoleEvent) {
		log.info(e)
		e.guild.channels.firstTextChannel().sendMessage("Added ${e.roleDefinition.name} @ ${e.roleDefinition.color}")
	}

	@Subscribe
	fun guildDeleteRole(e: GuildDeletedRoleEvent) {
		log.info(e)
		e.guild.channels.firstTextChannel().sendMessage("Deleted ${e.roleDefinition.name} @ ${e.roleDefinition.color}")
	}

	@Subscribe
	fun guildUpdateRole(e: GuildUpdatedRoleEvent) {
		log.info(e)
		e.guild.channels.firstTextChannel().sendMessage("Updated ${e.roleDefinition.name} @ ${e.roleDefinition.color}")
	}

	@Subscribe
	fun guildUpdateGuild(e: GuildUpdatedGuildEvent) {
		log.info(e)
		e.guild.channels.firstTextChannel().sendMessage("服务器更新了。")
	}

	@Subscribe
	fun guildDeleteGuild(e: GuildDeletedGuildEvent) {
		log.info(e)
		e.guild.users.first().sendMessage("${e.guild.kView.name} 服务器删除了。")
	}

	@Subscribe
	fun guildAddBlockList(e: GuildAddedBlockListEvent) {
		log.info(e)
		e.guild.channels.firstTextChannel().sendMessage("${e.userIds.first()} 因 ${e.remark} 被禁封了。")
	}

	@Subscribe
	fun guildDeletedBlockList(e: GuildDeletedBlockListEvent) {
		log.info(e)
		e.guild.channels.firstTextChannel().sendMessage("${e.userIds.first()} 被解除禁封了。")
	}

	val table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF"
	val tr = mutableMapOf<Char, Int>().apply {
		for(i in 0 until 58) {
			this[table[i]] = i
		}
	}
	val s = arrayOf(11, 10, 3, 8, 4, 6, 2, 9, 5, 7)
	val xor = 177451812L
	val add = 8728348608L

	fun String.toAV2(): String {
		var r = 0L
		for(i in 0 until 6) {
			r += tr[this[s[i]]]!! * 58.0.times(i.toDouble()).toLong()
		}
		return "av${(r-add) xor xor}"
	}

	companion object {

		@JvmStatic
		fun main(args: Array<String>) {
			TestClient().start()
		}
	}

}
