package kaikt.websocket.yeti.command

import kaikt.websocket.yeti.YetiBot
import kaikt.websocket.yeti.sender.Sender
import kaikt.websocket.yeti.sender.guild.Channel
import kaikt.websocket.yeti.sender.guild.Guild
import kaikt.websocket.yeti.sender.user.User

/**
 * @param bot 机器人实例
 * @param content 消息内容
 * @param sender 消息来源（频道或用户）
 * @param authorSender 消息发送者（用户）
 * @param authorId 消息发送者ID
 * @param senderId 消息来源ID（频道ID或ChatCode）
 * @param messageId 消息ID
 * @param messageTimestamp 消息时间戳
 */
sealed class MessageContext(
	val bot: YetiBot,

	val content: String,
	open val sender: Sender,
	open val authorSender: Sender,

	val authorId: String,
	val senderId: String,
	val messageId: String,
	val messageTimestamp: Long
)

/**
 * @param chatCode 私聊ID
 * @see MessageContext
 */
class DirectMessageContext(
	bot: YetiBot,

	content: String,
	override val sender: User,
	override val authorSender: User,

	authorId: String,
	chatCode: String,
	messageId: String,
	messageTimestamp: Long
) : MessageContext(bot, content, sender, authorSender, authorId, chatCode, messageId, messageTimestamp)

/**
 * @param guildId 服务器ID
 * @param guildInst 服务器实例
 * @param mention 被提及用户ID列表
 * @param mentionAll 提及所有人
 * @param mentionHere 提及所有此频道用户
 * @param mentionRoles 提及身分组ID列表
 * @see MessageContext
 */
class ChannelMessageContext(
	bot: YetiBot,

	content: String,
	override val sender: Channel,
	override val authorSender: User,

	authorId: String,
	senderId: String,
	messageId: String,
	messageTimestamp: Long,

	val guildId: String,
	val guildInst: Guild,

	val mention: List<String>,
	val mentionAll: Boolean,
	val mentionHere: Boolean,
	val mentionRoles: List<String>
) : MessageContext(bot, content, sender, authorSender, authorId, senderId, messageId, messageTimestamp)

fun MessageContext.reply(content: String) = sender.sendMessage(content)
fun MessageContext.replyQuote(content: String) = sender.sendMessage(content, quote = messageId)

fun ChannelMessageContext.isBotMentioned(): Boolean = mentionAll || bot.selfView.id in mention