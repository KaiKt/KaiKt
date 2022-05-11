package kaikt.api.entity.definition

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kaikt.gson

typealias Typed = JsonElement

typealias Embed = Typed
typealias Attachment = Typed

data class KMessageDefinition(
	val id: String,
	val type: Int,
	val content: String,
	val mention: List<String>,
	@SerializedName("mention_all")
	val mentionAll: Boolean,
	@SerializedName("mention_roles")
	val mentionRoles: List<String>,
	@SerializedName("mention_here")
	val mentionHere: Boolean,
	val embeds: List<Embed?>,
	val attachments: Attachment?,
	@SerializedName("created_at")
	val createdAt: Long,
	@SerializedName("updated_at")
	val updatedAt: Long,
	val reactions: List<KReactionDefinition>,
	val author: KUserDefinition,
	@SerializedName("image_name")
	val imageName: String,
	@SerializedName("read_status")
	val readStatus: Boolean,
	val quote: KMessageReplyDefinition?,
	@SerializedName("mention_info")
	val mentionInfo: KMentionInfoDefinition
)

fun Typed?.peekType(): String? {
	return this?.asJsonObject?.getAsJsonPrimitive("type")?.asString
}

fun Embed.toBiliVideo(): EmbedBiliVideo {
	if(peekType() == "bili-video") {
		return gson.fromJson(this, EmbedBiliVideo::class.java)
	} else {
		throw UnsupportedOperationException("${this.javaClass.simpleName} cannot cast to EmbedBiliVideo")
	}
}

fun Attachment.toImageAttachment(): ImageAttachment {
	if(peekType() == "image") {
		return gson.fromJson(this, ImageAttachment::class.java)
	} else {
		throw UnsupportedOperationException("${this.javaClass.simpleName} cannot cast to ImageAttachment")
	}
}

fun Attachment.toFileAttachment(): FileAttachment {
	if(peekType() == "file") {
		return gson.fromJson(this, FileAttachment::class.java)
	} else {
		throw UnsupportedOperationException("${this.javaClass.simpleName} cannot cast to FileAttachment")
	}
}

fun Attachment.toVideoAttachment(): VideoAttachment {
	if(peekType() == "video") {
		return gson.fromJson(this, VideoAttachment::class.java)
	} else {
		throw UnsupportedOperationException("${this.javaClass.simpleName} cannot cast to VideoAttachment")
	}
}

data class ImageAttachment(
	val type: String,
	val name: String,
	val url: String
)

data class FileAttachment(
	val type: String,
	val url: String,
	val name: String,
	@SerializedName("file_type")
	val fileType: String,
	val size: Long
)

data class VideoAttachment(
	val type: String,
	val url: String,
	val name: String,
	val duration: Long,
	val size: Long,
	val width: Int,
	val height: Int
)

data class EmbedBiliVideo(
	val type: String,
	val url: String,
	@SerializedName("origin_url")
	val originUrl: String,
	/**
	 * AV 号，现在实际上是 BV 号
	 */
	@SerializedName("av_no")
	val avNumber: String,
	@SerializedName("iframe_path")
	val iframePath: String,
	val duration: Long,
	val title: String,
	val pic: String
)