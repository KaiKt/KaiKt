package kaikt.api.entity.request

import kaikt.api.toInt
import kaikt.api.util.valueNotNullMapOf

class KMessageListRequest(val targetId: String) {

	/**
	 * 参考消息ID
	 */
	var msgId: String? = null

	/**
	 * 是否查询置顶消息（置顶消息只支持查询最新的消息）
	 */
	var pin: Boolean? = null

	/**
	 * [查询模式][QueryFlag]
	 */
	var flag: QueryFlag? = null

	/**
	 * 当前分页消息数量
	 */
	var pageSize: Int? = null

	val query: Map<String, Any>
		get() = valueNotNullMapOf(
			"target_id" to targetId,
			"msg_id" to msgId,
			"pin" to pin?.toInt(),
			"flag" to flag?.serializedName,
			"page_size" to pageSize
		)

}