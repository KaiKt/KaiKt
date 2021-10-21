package kaikt

import kaikt.api.KToken
import kaikt.api.entity.enum.KGuildMuteType
import kaikt.api.entity.permission.PermissionEnum
import kaikt.api.entity.request.RoleIdOrUserId
import kaikt.api.entity.request.TargetIdOrChatCode
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.random.Random
import kotlin.random.nextUInt

class TestApi {

	private val log = LoggerFactory.getLogger("TestApi")
	private val api = KToken(KToken.TokenType.Bot, "1/MTA0MjE=/zN5Lh9Na4wZPk0fpeTjDIg==").toApi()

	private val guildId = "1848030750973248"
	private val userIdTaskeren = "3865457829"

	@Test
	fun testGateway() {
		api.Gateway().getGateway().let {
			println(it)
		}
	}

	@Test
	fun testGuildList() {
		log.info("测试 guild/list")
		api.Guild().getGuildList().let { guilds ->
			guilds.data.items.forEach { component ->
				println("服务器：$component")
			}

			val id = guilds.data.items.first().id


			println()
			println("测试 guild/view")
			println(api.Guild().getGuildView(id))


			println()
			println("测试 guild/user-list")
			api.Guild().getGuildUserList(id).data.items.forEach {
				println(it)
			}
		}
	}

	@Test
	fun testGuildNickname() {
		val nickname = Random.nextUInt(0U..UInt.MAX_VALUE)

		log.info(
			"{}", api.Guild().postGuildNickname(guildId, userIdTaskeren, nickname.toString())
		)
	}

	@Test
	fun testGuildLeave() {
		val guildId = "1848030750973248"

		log.info(
			"{}", api.Guild().postGuildLeave(guildId)
		)
	}

	@Test
	fun testKickOut() {
		val guildId = "1848030750973248"
		val targetId = "286234688"

		log.info(
			"{}", api.Guild().postKickOut(guildId, targetId)
		)
	}

	@Test
	fun testGuildMuteList() {
		log.info(
			"{}", api.Guild().getGuildMuteList(guildId)
		)
	}

	@Test
	fun testGuildMuteCreate() {
		log.info(
			"{}", api.Guild().postGuildMuteCreate(guildId, userIdTaskeren, KGuildMuteType.Headset)
		)
	}

	@Test
	fun testGuildMuteDelete() {
		log.info(
			"{}", api.Guild().postGuildMuteDelete(guildId, userIdTaskeren, KGuildMuteType.Headset)
		)
	}

	@Test
	fun testChannelList() {
		api.Channel().getChannelList(guildId).data.items.forEach {
			log.info("$it")
		}
	}

	@Test
	fun testChannelView() {
		val channel = "2164858811059799"
		log.info("${api.Channel().getChannelView(channel)}")
	}

	@Test
	fun testChannelCreate() {
		log.info("${api.Channel().postChannelCreate(guildId, "测试新建频道")}")
	}

	@Test
	fun testChannelDelete() {
		val channel = api.Guild().getGuildView(guildId).data.channels.first { it.name == "测试新建频道" }
		log.info("识别到测试 Channel ${channel.name}(${channel.id})，执行删除")
		log.info("${api.Channel().postChannelDelete(channel.id)}")
	}

	@Test
	fun testChannelMoveUser() {
		val voiceChannel = "5619366156155726"
		log.info("${api.Channel().postChannelMoveUser(voiceChannel, userIdTaskeren)}")
	}

	@Test
	fun testChannelRoleIndex() {
		log.info("${api.Channel().getChannelRoleIndex("2164858811059799")}")
		log.info("${api.Channel().getChannelRoleIndex("5619366156155726")}")
	}

	@Test
	fun testChannelRoleCreate() {
		log.info("${api.Channel().postChannelRoleCreate("5619366156155726", RoleIdOrUserId.withUserId(userIdTaskeren))}")
	}

	@Test
	fun testChannelRoleUpdate() {
		log.info("${api.Channel().postChannelRoleUpdate("5619366156155726", RoleIdOrUserId.withUserId(userIdTaskeren), 0b1).data.asPermissionUser}")
		val role = api.Guild().getGuildView(guildId).data.roles.first()
		log.info("${api.Channel().postChannelRoleUpdate("5619366156155726", RoleIdOrUserId.withRoleId(role.roleId), PermissionEnum.ManageChannel.toInt()).data.asPermissionRole}")
	}

	@Test
	fun testChannelRoleDelete() {
		log.info("${api.Channel().postChannelRoleDelete("5619366156155726", RoleIdOrUserId.withUserId(userIdTaskeren))}")
	}

	@Test
	fun testMessageList() {
		log.info("${api.Message().getMessageList("2164858811059799")}")
	}

	@Test
	fun testGetLastMessage() {
		log.info(
			"${
				api.Message().getMessageList("2164858811059799") {
					pageSize = 1
				}
			}"
		)
	}

	@Test
	fun testSendMessage() {
		log.info("${api.Message().postMessageCreate("2164858811059799", "我四撒币")}")
	}

	@Test
	fun testSendMessageAndUpdate() {
		val firstMsg = api.Message().postMessageCreate("2164858811059799", "测试文本#1")
		log.info("Message #1 ${firstMsg.data.msgId}")
		api.Message().postMessageUpdate(firstMsg.data.msgId, "测试文本#2")
	}

	@Test
	fun testSendMessageAndDelete() {
		val firstMsg = api.Message().postMessageCreate("2164858811059799", "测试文本#1")
		log.info("Message #1 ${firstMsg.data.msgId}")
		api.Message().postMessageDelete(firstMsg.data.msgId)
	}

	@Test
	fun testGetMessageReactionList() {
		log.info(
			"${
				api.Message()
					.getMessageReactionList("557fc015-5f82-4923-a7de-712c9342a002", "1848030750973248/YfGQy16Jzs02d01s")
			}"
		)
	}

	@Test
	fun testAddReaction() {
		log.info(
			"${
				api.Message()
					.postMessageAddReaction("557fc015-5f82-4923-a7de-712c9342a002", "1848030750973248/YfGQy16Jzs02d01s")
			}"
		)
	}

	@Test
	fun testDelReaction() {
		log.info(
			"${
				api.Message().postMessageDeleteReaction(
					"557fc015-5f82-4923-a7de-712c9342a002",
					"1848030750973248/YfGQy16Jzs02d01s",
					userIdTaskeren
				)
			}"
		)
	}

	@Test
	fun testGetUserChatList() {
		log.info("${api.UserChat().getUserChatList()}")
	}

	@Test
	fun testGetUserChatView() {
		log.info("${api.UserChat().getUserChatView("7775969d3b084e6fb3a55a482c191bc6")}")
	}

	@Test
	fun testUserChatCreate() {
		val create = api.UserChat().postUserChatCreate(userIdTaskeren)
		log.info("$create")
		log.info("${api.UserChat().postUserChatDelete(userIdTaskeren)}")
	}

	@Test
	fun testGuildEmojiList() {
		api.GuildEmoji().getGuildEmojiList(guildId).data.items.forEach {
			log.info("$it")
		}
	}

	@Test
	fun testDMList() {
		api.DirectMessage().getDirectMessageList(TargetIdOrChatCode.withTargetId(userIdTaskeren)).data.items.forEach {
			log.info("$it")
		}
	}

	@Test
	fun testDMCreate() {
		log.info(
			"${api.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withTargetId(userIdTaskeren), "测试的消息")}"
		)
	}

	@Test
	fun testDMUpdate() {
		val firstDM = api.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withTargetId(userIdTaskeren), "更新的消息 #0")
		log.info("第一条消息：${firstDM.data.msgId}")
		log.info("${api.DirectMessage().postDirectMessageUpdate(firstDM.data.msgId, "更新后的消息 #1")}")
	}

	@Test
	fun testDMDelete() {
		val firstDM = api.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withTargetId(userIdTaskeren), "要删除的消息")
		log.info("第一条消息：${firstDM.data.msgId}")
		log.info("${api.DirectMessage().postDirectMessageDelete(firstDM.data.msgId)}")
	}

	@Test
	fun testGetRoles() {
		api.GuildRole().getGuildRoleList(guildId).data.items.forEach {
			log.info("$it")
		}
	}

	@Test
	fun testCreateAndUpdateRoleThenDelete() {
		val role1 = api.GuildRole().postGuildRoleCreate(guildId, "测试身分组")
		log.info("$role1")
		log.info("${
			api.GuildRole().postGuildRoleUpdate(guildId, role1.data.roleId) {
				name = "测试身分组改名了"
			}
		}")
		log.info("${api.GuildRole().postGuildRoleDelete(guildId, role1.data.roleId)}")
	}

	@Test
	fun testRoleGrantAndRevoke() {
		val role = api.GuildRole().getGuildRoleList(guildId).data.items.first { it.type == 0 && it.permissions != 1 }
		log.info("${api.GuildRole().postGuildRoleGrant(guildId, userIdTaskeren, role.roleId)}")
		log.info("${api.GuildRole().postGuildRoleRevoke(guildId, userIdTaskeren, role.roleId)}")
	}

	@Test
	fun testGuildEmojiUpload() {
		log.info(
			"${
				api.GuildEmoji()
					.postGuildEmojiCreate(guildId, File("C:\\Users\\r0yal\\Pictures\\QQ图片20211005212754.png"), "测试表情")
			}"
		)
	}

	@Test
	fun testGuildEmojiUpdate() {
		val emojiId = api.GuildEmoji().getGuildEmojiList(guildId).data.items.first().id
		log.info("${api.GuildEmoji().postGuildEmojiUpdate(emojiId, "测试表情 ${Random.nextUInt()}")}")
	}

	@Test
	fun testGuildEmojiDelete() {
		log.info("${api.GuildEmoji().postGuildEmojiDelete("1848030750973248/Etu1I1WMIl02d01s")}")
	}
}