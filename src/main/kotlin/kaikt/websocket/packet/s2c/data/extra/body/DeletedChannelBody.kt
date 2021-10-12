package kaikt.websocket.packet.s2c.data.extra.body

import com.google.gson.annotations.SerializedName

data class DeletedChannelBody(
	val id: String,
	@SerializedName("deleted_at")
	val deletedAt: Long
)
