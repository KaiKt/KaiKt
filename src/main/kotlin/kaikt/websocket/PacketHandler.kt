package kaikt.websocket

import kaikt.api.entity.definition.*
import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.enum.toMessageType
import kaikt.websocket.event.direct.*
import kaikt.websocket.event.guild.*
import kaikt.websocket.packet.s2c.S2CEventPacket
import kaikt.websocket.packet.s2c.data.ChannelType
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory
import java.lang.RuntimeException

private val logger = LoggerFactory.getLogger("EventPacketHandler")

class PacketHandler(private val client: KaiClient) {

	val bus = EventBus()

	var sn = 0

	fun resetSn() {
		sn = 0
	}

	fun handle(packet: S2CEventPacket) {
		if(packet.sn-1 == sn) {
			sn += 1
			logger.debug("$packet")

			// 跳过自己的消息，但是其他的还是需要自行判断处理
			if(packet.data.authorId == client.me.userId) {
				return
			}

			val data = packet.data

			val from = data.channelType
			val type = data.type

			if(data.isSystemType) {
				val extra = data.systemExtra

				when(extra.type) {
					"added_reaction" -> {
						val e = extra.asReactionBody!!
						if(e.userId != client.me.userId) {
							bus.post(GuildAddedReactionEvent(client, e.channelId, e.emoji, e.userId, e.msgId))
						}
					}
					"deleted_reaction" -> {
						val e = extra.asReactionBody!!
						if(e.userId != client.me.userId) {
							bus.post(GuildDeletedReactionEvent(client, e.channelId, e.emoji, e.userId, e.msgId))
						}
					}
					"updated_message" -> { // TODO: 2021/10/7 没有判断是否是自己发送的办法
						val e = extra.asUpdatedMessageBody!!
						bus.post(GuildUpdatedMessageEvent(client, data.targetId, e.channelId, e.content, e.mention, e.mentionAll, e.mentionHere, e.mentionRoles, e.msgId))
					}

					// 2021/10/9 后添加的内容

					"deleted_message" -> {
						val e = extra.asDeletedMessageBody!!
						bus.post(GuildDeletedMessageEvent(client, data.targetId, e.channelId, e.msgId))
					}

					// TODO: 2021/10/10 开黑啦摆烂，改文档也不该错接口，那我也摆烂。
					"added_channel" -> {
						val e = extra.asChannelBody!!
						kotlin.runCatching {
							bus.post(GuildAddedChannelEvent(client, e.guildId, e))
						}.onFailure {
							throw RuntimeException("开黑啦摆烂，改文档也不该错接口，那我也摆烂。", it)
						}
					}
					"updated_channel" -> {
						val e = extra.asChannelBody!!
						kotlin.runCatching {
							bus.post(GuildUpdatedChannelEvent(client, e.guildId, e))
						}.onFailure {
							throw RuntimeException("开黑啦摆烂，改文档也不该错接口，那我也摆烂。", it)
						}
					}
					"deleted_channel" -> {
						val e = extra.asDeletedChannelBody!!
						bus.post(GuildDeletedChannelEvent(client, data.targetId, e.id))
					}
					"pinned_message" -> {
						val e = extra.asPinMessageBody!!
						bus.post(GuildPinnedMessageEvent(client, data.targetId, e.channelId, e.operatorId, e.msgId))
					}
					"unpinned_message" -> {
						val e = extra.asPinMessageBody!!
						bus.post(GuildUnpinnedMessageEvent(client, data.targetId, e.channelId, e.operatorId, e.msgId))
					}

					"updated_private_message" -> {
						val e = extra.asUpdatedPrivateMessageBody!!
						bus.post(PrivateUpdatedMessageEvent(client, e.content, e.authorId, e.msgId, e.updatedAt, e.chatCode))
					}
					"deleted_private_message" -> {
						val e = extra.asDeletedPrivateMessageBody!!
						bus.post(PrivateDeletedMessageEvent(client, e.authorId, e.msgId, e.deletedAt, e.chatCode))
					}
					"private_added_reaction" -> {
						val e = extra.asPrivateReactionBody!!
						if(e.userId != client.me.userId) {
							bus.post(PrivateAddedReactionEvent(client, e.chatCode, e.emoji, e.userId, e.msgId))
						}
					}
					"private_deleted_reaction" -> {
						val e = extra.asPrivateReactionBody!!
						if(e.userId != client.me.userId) {
							bus.post(PrivateDeletedReactionEvent(client, e.chatCode, e.emoji, e.userId, e.msgId))
						}
					}

					"joined_guild" -> {
						val e = extra.asJoinedGuildBody!!
						bus.post(GuildUserJoinedEvent(client, data.targetId, e.userId, e.joinedAt))
					}
					"exited_guild" -> {
						val e = extra.asExitedGuildBody!!
						bus.post(GuildUserExitedEvent(client, data.targetId, e.userId, e.exitedAt))
					}

					"updated_guild_member" -> {
						val e = extra.asUpdatedGuildMemberBody!!
						if(e.userId != client.me.userId) {
							bus.post(GuildUserUpdatedEvent(client, data.targetId, e.userId, e.nickname))
						}
					}
					"guild_member_online" -> {
						val e = extra.asGuildMemberBody!!
						bus.post(GuildMemberOnlineEvent(client, data.targetId, e.userId, e.eventTime, e.guilds))
					}
					"guild_member_offline" -> {
						val e = extra.asGuildMemberBody!!
						bus.post(GuildMemberOfflineEvent(client, data.targetId, e.userId, e.eventTime, e.guilds))
					}

					"added_role" -> {
						val e = extra.asGuildRoleBody!!
						bus.post(GuildAddedRoleEvent(client, data.targetId, e))
					}
					"deleted_role" -> {
						val e = extra.asGuildRoleBody!!
						bus.post(GuildDeletedRoleEvent(client, data.targetId, e))
					}
					"updated_role" -> {
						val e = extra.asGuildRoleBody!!
						bus.post(GuildUpdatedRoleEvent(client, data.targetId, e))
					}

					"updated_guild" -> {
						val e = extra.asGuildBody!!
						bus.post(GuildUpdatedGuildEvent(client, data.targetId, e))
					}
					"deleted_guild" -> {
						val e = extra.asGuildBody!!
						bus.post(GuildDeletedGuildEvent(client, data.targetId, e))
					}

					"added_block_list" -> {
						val e = extra.asGuildBanBody!!
						bus.post(GuildAddedBlockListEvent(client, data.targetId, e.operatorId, e.remark!!, e.userId))
					}
					"deleted_block_list" -> {
						val e = extra.asGuildBanBody!!
						bus.post(GuildDeletedBlockListEvent(client, data.targetId, e.operatorId, e.userId))
					}
				}
			} else {
				val extra = data.nonSystemExtra

				when(type.toMessageType()) {
					KMessageType.Text -> { // 文字消息
						if(from == ChannelType.PERSON) {
							bus.post(PrivateTextMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author))
						} else {
							bus.post(GuildTextMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author))
						}
					}
					KMessageType.Picture -> { // 图片消息
						if(from == ChannelType.PERSON) {
							bus.post(PrivateImageMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.attachments.toImageAttachment()))
						} else {
							bus.post(GuildImageMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.attachments.toImageAttachment()))
						}
					}
					KMessageType.Video -> { // 视频消息
						if(from == ChannelType.PERSON) {
							bus.post(PrivateVideoMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.attachments.toVideoAttachment()))
						} else {
							bus.post(GuildVideoMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.attachments.toVideoAttachment()))
						}
					}
					KMessageType.File -> { // 文件消息
						if(from == ChannelType.PERSON) {
							bus.post(PrivateFileMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.attachments.toFileAttachment()))
						} else {
							bus.post(GuildFileMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.attachments.toFileAttachment()))
						}
					}
					KMessageType.KMarkdown -> { // KMarkdown消息
						if(from == ChannelType.PERSON) {
							bus.post(PrivateKMarkdownMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.kMarkdown!!))
						} else {
							bus.post(GuildKMarkdownMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.kMarkdown!!))
						}
					}
					KMessageType.CardMessage -> {
						if(from == ChannelType.PERSON) {
							bus.post(PrivateCardMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.kMarkdown!!))
						} else {
							bus.post(GuildCardMessageEvent(client, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.kMarkdown!!))
						}
					}
					else -> {
						logger.warn("未知的消息类型（消息类型=$type）：$packet")
					}
				}
			}
		} else {
			logger.warn("我们收到了来自未来的数据？数据的sn为 ${packet.sn}，目前最新的已处理的 sn 为 ${sn}。")
		}
	}
}