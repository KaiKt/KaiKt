package kaikt.websocket.hazelnut.guild

import kaikt.api.KaiApi
import kaikt.api.entity.definition.KChannelDefinition
import kaikt.api.entity.definition.KChannelViewDefinition
import kaikt.websocket.hazelnut.toHUser

data class HChannel(
	private val api: KaiApi,
	val guild: HGuild?,
	val channelId: String,
	val isCategory: Boolean? = null,
	val isVoice: Boolean? = null
) {

	// KaiKt

	val kView get() = api.Channel().getChannelView(channelId)

	// Hazelnut

	fun delete() {
		api.Channel().postChannelDelete(channelId)
	}

	// TODO: 2021/10/7 应该有全移走的方法

	fun sendMessage(content: String, quote: HGuildMessage? = null): HGuildMessage {
		val create = api.Message().postMessageCreate(channelId, content) { this.quote = quote?.messageId }
		return HGuildMessage(api, 1, this, create.msgId, content, api.me.toHUser(api, guild))
	}

}

fun KChannelDefinition.toHChannel(api: KaiApi) = HChannel(api, HGuild(api, guildId), id, isCategory)
fun KChannelDefinition.toHChannel(api: KaiApi, guild: HGuild) = HChannel(api, guild, id, isCategory)

fun KChannelViewDefinition.toHChannel(api: KaiApi) = HChannel(api, HGuild(api, guildId), id, isCategory)
fun KChannelViewDefinition.toHChannel(api: KaiApi, guild: HGuild) = HChannel(api, guild, id, isCategory)