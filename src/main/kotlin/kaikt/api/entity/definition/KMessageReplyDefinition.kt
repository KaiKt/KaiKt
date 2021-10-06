package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

/**
 * 回复消息里用到的精简版 [KMessageDefinition]
 */
data class KMessageReplyDefinition(
	val id: String,
	val type: Int,
	val content: String,
	@SerializedName("created_at")
	val createdAt: Long,
	val author: KUserDefinition
)
