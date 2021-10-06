package kaikt.api.entity.request

import kaikt.api.util.valueNotNullMapOf

class KDirectMessageRequest(val targetIdOrChatCode: TargetIdOrChatCode) {

	/**
	 * 参考消息ID
	 */
	var msgId: String? = null

	/**
	 * [查询模式][QueryFlag]
	 */
	var flag: QueryFlag? = null

	/**
	 * 目标页数
	 */
	var page: Int? = null

	/**
	 * 每页数据量（默认50）
	 */
	var pageSize: Int? = null

	val query: Map<String, Any>
		get() = valueNotNullMapOf(
			"chat_code" to targetIdOrChatCode.chatCode,
			"target_id" to targetIdOrChatCode.targetId,
			"msg_id" to msgId,
			"flag" to flag?.serializedName,
			"page" to page,
			"page_size" to pageSize
		)

}