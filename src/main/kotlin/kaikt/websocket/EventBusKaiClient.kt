package kaikt.websocket

import kaikt.api.KaiApi
import kaikt.api.entity.definition.toFileAttachment
import kaikt.api.entity.definition.toImageAttachment
import kaikt.api.entity.definition.toVideoAttachment
import kaikt.api.entity.enum.KMessageType
import kaikt.api.entity.enum.toMessageType
import kaikt.websocket.event.direct.*
import kaikt.websocket.event.guild.*
import kaikt.websocket.packet.s2c.S2CEventPacket
import kaikt.websocket.packet.s2c.data.ChannelType
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory

class EventBusKaiClient(api: KaiApi, val eventBus: EventBus = EventBus()) : KaiClient(api) {

	private val logger = LoggerFactory.getLogger("EventBusKaiClient")

	override suspend fun processEvent(packet: S2CEventPacket) {
		dispatchEvent(packet)
	}

	@Suppress("DEPRECATION")
	private fun dispatchEvent(packet: S2CEventPacket) {
		val data = packet.data

		val from = data.channelType
		val type = data.type

		if(data.isSystemType) {
			val extra = data.systemExtra

			when(extra.type) {
				"added_reaction" -> {
					val e = extra.asReactionBody!!
					if(e.userId != me.id) {
						eventBus.post(GuildAddedReactionEvent(this, e.channelId, e.emoji, e.userId, e.msgId))
					}
				}
				"deleted_reaction" -> {
					val e = extra.asReactionBody!!
					if(e.userId != this.me.id) {
						eventBus.post(GuildDeletedReactionEvent(this, e.channelId, e.emoji, e.userId, e.msgId))
					}
				}
				"updated_message" -> { // TODO: 2021/10/7 没有判断是否是自己发送的办法
					val e = extra.asUpdatedMessageBody!!
					eventBus.post(GuildUpdatedMessageEvent(this, data.targetId, e.channelId, e.content, e.mention, e.mentionAll, e.mentionHere, e.mentionRoles, e.msgId, e.updatedAt))
				}

				// 2021/10/9 后添加的内容

				"deleted_message" -> {
					val e = extra.asDeletedMessageBody!!
					eventBus.post(GuildDeletedMessageEvent(this, data.targetId, e.channelId, e.msgId))
				}

				"added_channel" -> {
					val e = extra.asChannelBody!!
					eventBus.post(GuildAddedChannelEvent(this, e.guildId, e))
				}
				"updated_channel" -> {
					val e = extra.asChannelBody!!
					eventBus.post(GuildUpdatedChannelEvent(this, e.guildId, e))
				}
				"deleted_channel" -> {
					val e = extra.asDeletedChannelBody!!
					eventBus.post(GuildDeletedChannelEvent(this, data.targetId, e.id))
				}
				"pinned_message" -> {
					val e = extra.asPinMessageBody!!
					eventBus.post(GuildPinnedMessageEvent(this, data.targetId, e.channelId, e.operatorId, e.msgId))
				}
				"unpinned_message" -> {
					val e = extra.asPinMessageBody!!
					eventBus.post(GuildUnpinnedMessageEvent(this, data.targetId, e.channelId, e.operatorId, e.msgId))
				}

				"updated_private_message" -> {
					val e = extra.asUpdatedPrivateMessageBody!!
					eventBus.post(PrivateUpdatedMessageEvent(this, e.content, e.authorId, e.msgId, e.updatedAt, e.chatCode))
				}
				"deleted_private_message" -> {
					val e = extra.asDeletedPrivateMessageBody!!
					eventBus.post(PrivateDeletedMessageEvent(this, e.authorId, e.msgId, e.deletedAt, e.chatCode))
				}
				"private_added_reaction" -> {
					val e = extra.asPrivateReactionBody!!
					if(e.userId != this.me.id) {
						eventBus.post(PrivateAddedReactionEvent(this, e.chatCode, e.emoji, e.userId, e.msgId))
					}
				}
				"private_deleted_reaction" -> {
					val e = extra.asPrivateReactionBody!!
					if(e.userId != this.me.id) {
						eventBus.post(PrivateDeletedReactionEvent(this, e.chatCode, e.emoji, e.userId, e.msgId))
					}
				}

				"joined_guild" -> {
					val e = extra.asJoinedGuildBody!!
					eventBus.post(GuildUserJoinedEvent(this, data.targetId, e.userId, e.joinedAt))
				}
				"exited_guild" -> {
					val e = extra.asExitedGuildBody!!
					eventBus.post(GuildUserExitedEvent(this, data.targetId, e.userId, e.exitedAt))
				}

				"updated_guild_member" -> {
					val e = extra.asUpdatedGuildMemberBody!!
					if(e.userId != this.me.id) {
						eventBus.post(GuildUserUpdatedEvent(this, data.targetId, e.userId, e.nickname))
					}
				}
				"guild_member_online" -> {
					val e = extra.asGuildMemberBody!!
					eventBus.post(GuildMemberOnlineEvent(this, data.targetId, e.userId, e.eventTime, e.guilds))
				}
				"guild_member_offline" -> {
					val e = extra.asGuildMemberBody!!
					eventBus.post(GuildMemberOfflineEvent(this, data.targetId, e.userId, e.eventTime, e.guilds))
				}

				"added_role" -> {
					val e = extra.asGuildRoleBody!!
					eventBus.post(GuildAddedRoleEvent(this, data.targetId, e))
				}
				"deleted_role" -> {
					val e = extra.asGuildRoleBody!!
					eventBus.post(GuildDeletedRoleEvent(this, data.targetId, e))
				}
				"updated_role" -> {
					val e = extra.asGuildRoleBody!!
					eventBus.post(GuildUpdatedRoleEvent(this, data.targetId, e))
				}

				"updated_guild" -> {
					val e = extra.asGuildBody!!
					eventBus.post(GuildUpdatedGuildEvent(this, data.targetId, e))
				}
				"deleted_guild" -> {
					val e = extra.asGuildBody!!
					eventBus.post(GuildDeletedGuildEvent(this, data.targetId, e))
				}

				"added_block_list" -> {
					val e = extra.asGuildBanBody!!
					eventBus.post(GuildAddedBlockListEvent(this, data.targetId, e.operatorId, e.remark!!, e.userId))
				}
				"deleted_block_list" -> {
					val e = extra.asGuildBanBody!!
					eventBus.post(GuildDeletedBlockListEvent(this, data.targetId, e.operatorId, e.userId))
				}
			}
		} else {
			val extra = data.nonSystemExtra

			when(type.toMessageType()) {
				KMessageType.Text -> { // 文字消息
					if(from == ChannelType.PERSON) {
						eventBus.post(PrivateTextMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author))
					} else {
						eventBus.post(GuildTextMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author))
					}
				}
				KMessageType.Picture -> { // 图片消息
					if(from == ChannelType.PERSON) {
						eventBus.post(PrivateImageMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.attachments.toImageAttachment()))
					} else {
						eventBus.post(GuildImageMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.attachments.toImageAttachment()))
					}
				}
				KMessageType.Video -> { // 视频消息
					if(from == ChannelType.PERSON) {
						eventBus.post(PrivateVideoMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.attachments.toVideoAttachment()))
					} else {
						eventBus.post(GuildVideoMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.attachments.toVideoAttachment()))
					}
				}
				KMessageType.File -> { // 文件消息
					if(from == ChannelType.PERSON) {
						eventBus.post(PrivateFileMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.attachments.toFileAttachment()))
					} else {
						eventBus.post(GuildFileMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.attachments.toFileAttachment()))
					}
				}
				KMessageType.KMarkdown -> { // KMarkdown消息
					if(from == ChannelType.PERSON) {
						eventBus.post(PrivateKMarkdownMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.kMarkdown!!))
					} else {
						eventBus.post(GuildKMarkdownMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.kMarkdown!!))
					}
				}
				KMessageType.CardMessage -> {
					if(from == ChannelType.PERSON) {
						eventBus.post(PrivateCardMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.code, extra.author, extra.kMarkdown!!))
					} else {
						eventBus.post(GuildCardMessageEvent(this, data.content, data.authorId, data.targetId, data.msgId, data.msgTimestamp, extra.guildId, extra.channelName, extra.mention, extra.mentionAll, extra.mentionHere, extra.mentionRoles, extra.author, extra.kMarkdown!!))
					}
				}
				else -> {
					logger.warn("未知的消息类型（消息类型=$type）：$packet")
				}
			}
		}
	}
}