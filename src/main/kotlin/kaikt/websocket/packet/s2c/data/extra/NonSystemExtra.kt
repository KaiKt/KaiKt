package kaikt.websocket.packet.s2c.data.extra

import com.google.gson.annotations.SerializedName
import kaikt.api.entity.definition.*

data class NonSystemExtra(
	val type: Int,

	@SerializedName("guild_id")
	val guildId: String,

	@SerializedName("channel_name")
	val channelName: String,

	val mention: List<String>,

	@SerializedName("mention_all")
	val mentionAll: Boolean,

	@SerializedName("mention_roles")
	val mentionRoles: List<String>,

	@SerializedName("mention_here")
	val mentionHere: Boolean,

	val author: KUserDefinition,

	val attachments: Attachment,

	@SerializedName("kmarkdown")
	val kMarkdown: KMarkdownDefinition?,

	val code: String
)