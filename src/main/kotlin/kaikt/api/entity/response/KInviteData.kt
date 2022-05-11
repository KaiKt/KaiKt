package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.KUserDefinition
import java.net.URL

data class KInviteData(
	@SerializedName("guild_id")
	val guildId: String,
	@SerializedName("channel_id")
	val channelId: String,
	@SerializedName("url_code")
	val urlCode: String,
	val url: String,
	val user: KUserDefinition
) {

	fun getUrl(): URL = URL(url)
}