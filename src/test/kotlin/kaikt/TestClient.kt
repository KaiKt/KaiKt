@file:Suppress("SpellCheckingInspection")

package kaikt

import kaikt.api.KToken
import kaikt.api.entity.enum.KMessageType
import kaikt.cardmsg.*
import kaikt.websocket.KaiClient
import kaikt.websocket.event.direct.*
import kaikt.websocket.event.guild.*
import org.apache.logging.log4j.LogManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.SubscriberExceptionEvent

class TestClient {

	private val log = LogManager.getLogger("KaiClient")
	private val api = KToken(KToken.TokenType.Bot, "1/MTA0MjE=/zN5Lh9Na4wZPk0fpeTjDIg==").toApi()

	fun start() {
		val cli = KaiClient(api)

		cli.eventBus.register(this)

		cli.connect()
	}

	@Subscribe
	fun guildTextMessage(event: GuildTextMessageEvent) {
		log.info(event)
		event.message.reply("哦哦，知道了")

//		val card = buildCardMessage {
//			header("测试卡片")
//			textAndButton("测试文本", "嗯", "danger")
//			// image("https://www.bungie.net/common/destiny2_content/icons/2828804450abc6575609258a3479b19a.jpg")
//			textAndImage("这个是机炮", "https://www.bungie.net/common/destiny2_content/icons/2828804450abc6575609258a3479b19a.jpg", "sm")
//		}
//
//		val card1 = "[{\"type\":\"card\",\"theme\":\"secondary\",\"size\":\"lg\",\"modules\":[{\"type\":\"header\",\"text\":{\"type\":\"plain-text\",\"content\":\"测试卡片\"}},{\"type\":\"section\",\"text\":{\"type\":\"plain-text\",\"content\":\"测试文本\"},\"mode\":\"right\",\"accessory\":{\"type\":\"button\",\"theme\":\"danger\",\"text\":{\"type\":\"plain-text\",\"content\":\"嗯\"}}},{\"type\":\"section\",\"text\":{\"type\":\"plain-text\",\"content\":\"这个是机炮\"},\"mode\":\"right\",\"accessory\":{\"type\":\"image\",\"src\":\"https://www.bungie.net/common/destiny2_content/icons/2828804450abc6575609258a3479b19a.jpg\",\"size\":\"sm\"}}]}]"
//
//		println(card)
//		println(card1)
//
//		event.channel.sendMessage(card) {
//			type = KMessageType.CardMessage
//		}
//		event.channel.sendMessage(card1) {
//			type = KMessageType.CardMessage
//		}
	}

	@Subscribe
	fun privateTextMessage(event: PrivateTextMessageEvent) {
		log.info(event)
		event.message.reply("哦哦，好的好的")

		event.authorUser.sendMessage("你什么意思？").addReaction("[#129300;]")
		// event.sender.chat.sendMessage("你什么意思？").createReaction("[#129300;]")
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
		// event.message.createReaction(event.emoji.id)
		event.message.addReaction(event.emoji.id)
	}

	@Subscribe
	fun guildDeletedMessage(event: GuildDeletedMessageEvent) {
		log.info(event)
		event.channel.sendMessage("被删除的讯息：${event.messageId}")
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
		event.guild.getChannel { it.isTextChannel() }.sendMessage("额，频道被删除了欸！${event.channelId}")
		// event.guild.channels.first { it.isCategory != true }.sendMessage("额，频道被删了欸！${event.channelId}")
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
		event.author.sendMessage("被删除的讯息：${event.messageId}")
		// event.chat.sendMessage(event.deletedMessage.messageId)
	}

	@Subscribe
	fun privateAddReaction(event: PrivateAddedReactionEvent) {
		log.info(event)
		event.message.addReaction(event.emoji.id)
		// event.message.createReaction(event.emoji.id)
	}

	@Subscribe
	fun privateDeleteReaction(event: PrivateDeletedReactionEvent) {
		log.info(event)
		event.message.delReaction(event.emoji.id)
		// event.message.deleteReaction(event.emoji.id)
	}

	@Subscribe
	fun guildUserJoined(event: GuildUserJoinedEvent) {
		log.info(event)
		event.guild.getChannel { it.isTextChannel() }.sendMessage("欢迎 ${event.user.getName()} 加入伺服器！")
		// event.guild.channels.first { it.isCategory != true }.sendMessage("欢迎 ${event.user.kView.data.nickname}")
	}

	@Subscribe
	fun guildUserExited(event: GuildUserExitedEvent) {
		log.info(event)
		event.guild.getChannel { it.isTextChannel() }.sendMessage("再见啦 ${event.user.getName()}！")
		// event.guild.channels.first { it.isCategory != true }.sendMessage("再见 ${event.user.kView.data.username}")
	}

	@Subscribe
	fun guildUserUpdated(event: GuildUserUpdatedEvent) {
		log.info(event)
		event.guild.getChannel { it.isTextChannel() }.sendMessage("${event.nickname} 改名了！")
	}

	@Subscribe
	fun guildMemberOnline(event: GuildMemberOnlineEvent) {
		log.info(event)
		event.guild.getChannel { it.isTextChannel() }.sendMessage("${event.user.getNickname()} 上线了！")
	}

	@Subscribe
	fun guildMemberOffline(event: GuildMemberOfflineEvent) {
		log.info(event)
		event.guild.getChannel { it.isTextChannel() }.sendMessage("${event.user.getNickname()} 下线了！")
	}

	@Subscribe
	fun guildAddRole(e: GuildAddedRoleEvent) {
		log.info(e)
		e.guild.getChannel { it.isTextChannel() }.sendMessage("Added ${e.roleDefinition.name} @ ${e.roleDefinition.color}")
	}

	@Subscribe
	fun guildDeleteRole(e: GuildDeletedRoleEvent) {
		log.info(e)
		e.guild.getChannel { it.isTextChannel() }.sendMessage("Deleted ${e.roleDefinition.name} @ ${e.roleDefinition.color}")
	}

	@Subscribe
	fun guildUpdateRole(e: GuildUpdatedRoleEvent) {
		log.info(e)
		e.guild.getChannel { it.isTextChannel() }.sendMessage("Updated ${e.roleDefinition.name} @ ${e.roleDefinition.color}")
	}

	@Subscribe
	fun guildUpdateGuild(e: GuildUpdatedGuildEvent) {
		log.info(e)
		e.guild.getChannel { it.isTextChannel() }.sendMessage("服务器更新了。")
	}

	@Subscribe
	fun guildDeleteGuild(e: GuildDeletedGuildEvent) {
		log.info(e)
		e.guild.getUsers().first().sendMessage("${e.guild.getName()} 服务器删除了。")
	}

	@Subscribe
	fun guildAddBlockList(e: GuildAddedBlockListEvent) {
		log.info(e)
		e.guild.getChannel { it.isTextChannel() }.sendMessage("${e.userIds.first()} 因 ${e.remark} 被禁封了。")
	}

	@Subscribe
	fun guildDeletedBlockList(e: GuildDeletedBlockListEvent) {
		log.info(e)
		e.guild.getChannel { it.isTextChannel() }.sendMessage("${e.userIds.first()} 被解除禁封了。")
	}

	@Subscribe
	fun onError(e: SubscriberExceptionEvent) {
		log.error(e.throwable)
	}

	val table = ("fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF")
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
