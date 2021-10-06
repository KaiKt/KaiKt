package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

/**
 * 似乎是精简版的 [KUserDefinition]
 */
data class KMentionPartDefinition(
	val id: String,
	val username: String,
	@SerializedName("full_name")
	val fullName: String,
	val avatar: String
)