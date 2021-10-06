package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName

data class KMessageCreateData(
	@SerializedName("msg_id")
	val msgId: String,
	@SerializedName("msg_timestamp")
	val msgTimestamp: Long,
	val nonce: String?
)
