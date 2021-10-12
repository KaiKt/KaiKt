package kaikt.websocket.packet.s2c.data.extra

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kaikt.api.entity.definition.*
import kaikt.websocket.packet.s2c.data.extra.body.*
import kaikt.websocket.packet.s2c.data.gson

typealias SystemExtraBody = JsonObject

// 根据 Definition 来的 Body 类型
typealias ChannelBody = KChannelDefinition

data class SystemExtra(
	val type: String,
	val body: SystemExtraBody
) {
	private inline fun <reified T> SystemExtraBody.to(vararg allowTypes: String): T? {
		return if(type in allowTypes) gson.fromJson(this, (object : TypeToken<T>() {}.type)) else null
	}

	// 频道相关事件

	// 添加/移除反应
	val asReactionBody get() = body.to<ReactionBody>("added_reaction", "deleted_reaction")

	// 更新/删除消息
	val asUpdatedMessageBody get() = body.to<UpdatedMessageBody>("updated_message")
	val asDeletedMessageBody get() = body.to<DeletedMessageBody>("deleted_message")

	// 添加/更新/移除频道
	val asChannelBody get() = body.to<ChannelBody>("added_channel", "updated_channel")
	val asDeletedChannelBody get() = body.to<DeletedChannelBody>("deleted_channel")

	// 置顶/取消置顶消息
	val asPinMessageBody get() = body.to<PinMessageBody>("pinned_message", "unpinned_message")

	// 私聊消息事件

	// 更新/删除私聊信息
	val asUpdatedPrivateMessageBody get() = body.to<UpdatedPrivateMessageBody>("updated_private_message")
	val asDeletedPrivateMessageBody get() = body.to<DeletedPrivateMessageBody>("deleted_private_message")

	// 添加/移除私聊反应
	val asPrivateReactionBody get() = body.to<PrivateReactionBody>("private_added_reaction", "private_deleted_reaction")

	// 服务器成员相关事件

	// 加入/退出服务器
	val asJoinedGuildBody get() = body.to<JoinedGuildBody>("joined_guild")
	val asExitedGuildBody get() = body.to<ExitedGuildBody>("exited_guild")

	// 更新服务器成员（改名）
	val asUpdatedGuildMemberBody get() = body.to<UpdatedGuildMemberBody>("updated_guild_member")

	// 服务器成员上下线
	val asGuildMemberBody get() = body.to<GuildMemberBody>("guild_member_online", "guild_member_offline")

	// 服务器角色增删改
	val asGuildRoleBody get() = body.to<KRoleDefinition>("added_role", "deleted_role", "updated_role")

	// 服务器删改
	val asGuildBody get() = body.to<KGuildDefinition>("updated_guild", "deleted_guild")

	// 服务器禁封/解禁用户
	val asGuildBanBody get() = body.to<GuildBanBody>("added_block_list", "deleted_block_list")

	// 用户加入语音
	val asUserJoinedVoiceBody get() = body.to<UserJoinedVoiceBody>("joined_channel")

	// 用户退出语音
	val asUserExitedVoiceBody get() = body.to<UserExitedVoiceBody>("exited_channel")

	// 用户更新名字/头像 - 该事件与服务器无关, 遵循以下条件
	//
	//	仅当用户的 用户名 或 头像 变更时;
	//	仅通知与该用户存在关联的用户或 Bot: a. 存在聊天会话 b. 双方好友关系
	val asUserUpdatedBody get() = body.to<UserUpdatedBody>("user_updated")

	// 机器人（自己）被拉入/踢出服务器
	val asSelfGuildBody get() = body.to<SelfGuildBody>("self_joined_guild", "self_exited_guild")

	// 点击卡片消息的按钮
	val asMessageBtnClickBody get() = body.to<MessageBtnClickBody>("message_btn_click")
}
