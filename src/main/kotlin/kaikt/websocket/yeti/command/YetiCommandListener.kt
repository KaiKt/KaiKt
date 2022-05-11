package kaikt.websocket.yeti.command

import kaikt.websocket.event.direct.PrivateKMarkdownMessageEvent
import kaikt.websocket.event.direct.PrivateTextMessageEvent
import kaikt.websocket.event.guild.GuildKMarkdownMessageEvent
import kaikt.websocket.event.guild.GuildTextMessageEvent
import kaikt.websocket.yeti.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class YetiCommandListener(val bot: YetiBot) {

	@Subscribe(threadMode = ThreadMode.ASYNC)
	fun onGuildMessage(event: GuildKMarkdownMessageEvent) {
		execute(
			ChannelMessageContext(
				bot,
				event.content,
				event.channelId.getChannel(),
				event.authorId.getUser(),
				event.authorId,
				event.channelId,
				event.messageId,
				event.messageTimestamp,
				event.guildId,
				event.guildId.getGuild(),
				event.mention,
				event.mentionAll,
				event.mentionHere,
				event.mentionRoles,
				event
			)
		)
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	fun onPrivateMessage(event: PrivateKMarkdownMessageEvent) {
		execute(
			DirectMessageContext(
				bot,
				event.content,
				event.authorId.getUser(),
				event.authorId.getUser(),
				event.authorId,
				event.chatCode,
				event.messageId,
				event.messageTimestamp,
				event
			)
		)
	}

	abstract fun execute(context: MessageContext)

}