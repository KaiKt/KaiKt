package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KMarkdownDefinition(
	/**
	 * 不带格式的源文本内容
	 */
	@SerializedName("raw_content")
	val rawContent: String,
	@SerializedName("mention_part")
	val mentionPart: List<String>,
	@SerializedName("mention_role_part")
	val mentionRolePart: List<String>
)
