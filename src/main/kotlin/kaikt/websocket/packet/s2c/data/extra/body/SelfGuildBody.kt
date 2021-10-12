package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class SelfGuildBody(
	@SerializedName("guild_id")
	val guildId: String
)