package kaikt.api.entity.definition

import com.google.gson.annotations.SerializedName

data class KUserChatViewDefinition(
	/**
	 * 私聊ID
	 */
	val code: String,

	/**
	 * 上次阅读消息的时间（毫秒）
	 */
	@SerializedName("last_read_time")
	val lastReadTime: Long,

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
	 * 是否为好友关系
	 */
	@SerializedName("is_friend")
	val isFriend: Boolean,

	/**
	 * 是否屏蔽目标用户
	 */
	@SerializedName("is_blocked")
	val isBlocked: Boolean,

	/**
	 * 是否被目标用户屏蔽
	 */
	@SerializedName("is_target_blocked")
	val isTargetBlocked: Boolean,

	/**
	 * 目标用户私聊设置
	 */
	@SerializedName("target_chat_setting")
	val targetChatSetting: Int,

	/**
	 * 目标用户信息
	 */
	@SerializedName("target_info")
	val targetInfo: KUserDefinition
)