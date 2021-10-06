package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KUserChatDefinition(
	/**
	 * 私聊ID
	 */
	val code: String,

	/**
	 * 上次阅读消息的时间（毫秒）
	 */
	@SerializedName("list_read_time")
	val listReadTime: Long,

	/**
	 * 最新消息时间（毫秒）
	 */
	@SerializedName("latest_msg_time")
	val latestMsgTime: Long,

	/**
	 * 未读消息数
	 */
	@SerializedName("unread_count")
	val unreadCount: Int,

	/**
	 * 目标用户信息
	 */
	@SerializedName("target_info")
	val targetInfo: KUserDefinition,

	/**
	 * 连接类型
	 */
	@SerializedName("ws_type")
	val wsType: Int
)