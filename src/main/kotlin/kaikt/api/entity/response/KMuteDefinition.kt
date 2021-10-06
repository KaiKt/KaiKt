package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName

data class KMuteDefinition(val type: Int, @SerializedName("user_ids") val userIds: List<String>)
