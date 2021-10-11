package kaikt.websocket.hazelnut

import kaikt.api.KaiApi
import kaikt.api.entity.definition.KUserDefinition
import kaikt.api.entity.request.TargetIdOrChatCode
import kaikt.websocket.hazelnut.direct.HUserChat
import kaikt.websocket.hazelnut.guild.*

data class HUser(
	private val api: KaiApi,
	val userId: String,

	val guild: HGuild? = null, // 在私聊的环境下是 null
	val bot: Boolean? = null
) {

	val kView get() = api.User().getUserView(userId, guild?.guildId)

	val chat get(): HUserChat {
		val chat = api.UserChat().postUserChatCreate(userId)
		return HUserChat (api, chat.code, api.meUser, this)
	}

	val joinedGuilds = api.Guild().getGuildList().map { it.toHGuild(api) }

	val username get() = kView.username
	val nickname get() = kView.nickname

	fun sendMessage(content: String, quote: String? = null) {
		api.DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withTargetId(userId), content) {
			this.quote = quote
		}
	}

	fun changeGuildDisplayName(name: String, g: HGuild? = guild) {
		if(g == null) {
			throw NullPointerException("guild_id")
		}

		api.Guild().postGuildNickname(g.guildId, userId, name)
	}

	fun leaveGuild(g: HGuild? = guild) {
		if(g == null) {
			throw NullPointerException("guild_id")
		}

		api.Guild().postGuildLeave(g.guildId)
	}

	fun moveToVoice(c: HChannel) {
		api.Channel().postChannelMoveUser(c.channelId, userId)
	}

	fun grantRole(g: HGuild? = guild, role: HRole) {
		if(g == null) {
			throw NullPointerException("guild_id")
		}

		g.grantRole(this, role)
	}

	fun revokeRole(g: HGuild? = guild, role: HRole) {
		if(g == null) {
			throw NullPointerException("guild_id")
		}

		g.revokeRole(this, role)
	}
}

fun KUserDefinition.toHUser(api: KaiApi, guildId: HGuild? = null) = HUser(api, id, guildId, bot)