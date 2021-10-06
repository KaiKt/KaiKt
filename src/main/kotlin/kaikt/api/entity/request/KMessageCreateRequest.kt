package kaikt.api.entity.request

import kaikt.api.entity.enum.KMessageType

import kaikt.api.entity.definition.KMessageDefinition
import kaikt.api.util.valueNotNullMapOf

data class KMessageCreateRequest(
	val targetId: String,
	val content: String
) {

	/**
	 * 消息类型；1-文本消息，2-图片消息，3-视频消息，4-文件消息，9-KMarkdown消息，10-卡片消息
	 */
	var type: KMessageType? = null

	/**
	 * 回复引用某条消息的[ID][KMessageDefinition.id]
	 */
	var quote: String? = null

	var nonce: String? = null

	/**
	 * 用户id，如果传了，代表该消息是临时消息，该消息不会存数据库，但是会在频道内只给该用户推送临时消息。用于在频道内针对用户的操作进行单独的回应通知等。
	 */
	var tempTargetId: String? = null

	val body
		get() = valueNotNullMapOf(
			"target_id" to targetId,
			"content" to content,
			"type" to type?.i,
			"quote" to quote,
			"nonce" to nonce,
			"temp_target_id" to tempTargetId
		)

}
