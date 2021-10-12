package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class GuildBanBody(
	@SerializedName("operator_id")
	val operatorId: String,
	val remark: String?, // 解禁的时候为空
	@SerializedName("user_id")
	val userId: List<String>
)
