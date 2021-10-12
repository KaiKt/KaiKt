package kaikt.websocket.hazelnut.guild

import kaikt.api.KaiApi
import kaikt.api.entity.definition.KGuildDefinition
import kaikt.api.entity.enum.KChannelType
import kaikt.websocket.hazelnut.HUser
import kaikt.websocket.hazelnut.toHUser
import kaikt.websocket.packet.s2c.data.ChannelType

data class HGuild(
	private val api: KaiApi,
	val guildId: String
) {

	// KaiKt

	val kView get() = api.Guild().getGuildView(guildId).data
	val kGuildMutes get() = api.Guild().getGuildMuteList(guildId)

	// Hazelnut

	val users get() = api.Guild().getGuildUserList(guildId).data.items.map { it.toHUser(api, this) }
	val channels get() = api.Channel().getChannelList(guildId).data.items.map { it.toHChannel(api, this) }
	val roles get() = api.GuildRole().getGuildRoleList(guildId).data.items.map { it.toHRole(api) }

	fun changeUserDisplayName(name: String, user: HUser) = user.changeGuildDisplayName(name, this)

	fun kick(user: HUser) = api.Guild().postKickOut(guildId, user.userId)

	fun createGuildMute(user: HUser, type: Int) = api.Guild().postGuildMuteCreate(guildId, user.userId, type)
	fun deleteGuildMute(user: HUser, type: Int) = api.Guild().postGuildMuteDelete(guildId, user.userId, type)

	fun createChannel(name: String, type: KChannelType) = api.Channel().postChannelCreate(guildId, name) { setType(type) }.data.toHChannel(api, this)
	fun deleteChannel(channel: HChannel) = channel.delete()

	fun grantRole(user: HUser, role: HRole) = api.GuildRole().postGuildRoleGrant(guildId, user.userId, role.roleId)
	fun revokeRole(user: HUser, role: HRole) = api.GuildRole().postGuildRoleRevoke(guildId, user.userId, role.roleId)

	// Hazelnut 原生方法
	// fun getChannel(channelId: String) = HChannel(api, this, channelId, false)

}

fun KGuildDefinition.toHGuild(api: KaiApi) = HGuild(api, id)

fun List<HChannel>.firstTextChannel() = first { it.isCategory != true && it.isVoice != true }