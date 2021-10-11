package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class GuildMemberBody(
	@SerializedName("user_id")
	val userId: String,
	@SerializedName("event_time")
	val eventTime: Long,
	val guilds: List<String>
)
