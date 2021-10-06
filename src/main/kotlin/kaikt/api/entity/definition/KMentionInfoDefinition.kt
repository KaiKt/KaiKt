package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KMentionInfoDefinition(
	@SerializedName("mention_part")
	val mentionPart: List<KMentionPartDefinition>,
	@SerializedName("mention_role_part")
	val mentionRolePart: List<KRoleDefinition>
)
