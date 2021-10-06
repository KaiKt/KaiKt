package kaikt.api.entity.permission

class KPermission {

	/*
	 * 使用方法：
	 * val p = 3213213213321; // 目前权限，查询是否有 Administrator
	 * val isAdmin = (p.and(KPermission.Administrator) != 0)
	 */

}

object KPermissionBits {

	const val Administrator       = 0b00000000000000000000000000001 // 管理员
	const val ManageGuild         = 0b00000000000000000000000000010 // 服务器管理员
	const val ManageLog           = 0b00000000000000000000000000100 // 查看管理日志
	const val CreateInvite        = 0b00000000000000000000000001000 // 创建服务器邀请
	const val ManageInvite        = 0b00000000000000000000000010000 // 管理服务器邀请
	const val ManageChannel       = 0b00000000000000000000000100000 // 管理频道
	const val KickOutUser         = 0b00000000000000000000001000000 // 踢出用户
	const val BanUser             = 0b00000000000000000000010000000 // 禁封用户
	const val ManageCustomEmoji   = 0b00000000000000000000100000000 // 管理自定义表情
	const val ModifyDisplayName   = 0b00000000000000000001000000000 // 修改用户的服务器昵称
	const val ManageRole          = 0b00000000000000000010000000000 // 管理角色权限 - 拥有此权限成员可以创建新的角色和编辑删除低于该角色的身份。
	const val VisibleChannel      = 0b00000000000000000100000000000 // 查看文字、语音频道
	const val SendMessage         = 0b00000000000000001000000000000 // 发布消息
	const val ManageMessage       = 0b00000000000000010000000000000 // 管理消息
	const val UploadFile          = 0b00000000000000100000000000000 // 上传文件
	const val ConnectVoice        = 0b00000000000001000000000000000 // 语音连接
	const val ManageVoice         = 0b00000000000010000000000000000 // 语音管理
	const val MentionAll          = 0b00000000000100000000000000000 // @全体成员
	const val NewReaction         = 0b00000000001000000000000000000 // 添加反应
	const val AddReaction         = 0b00000000010000000000000000000 // 跟随添加反应
	const val ConnectVoicePassive = 0b00000000100000000000000000000 // 被动连接语音 - 拥有此限制的成员无法主动连接语音频道，只能在被动邀请或被人移动时，才可以进入语音频道。
	const val PressToSpeakOnly    = 0b00000001000000000000000000000 // 仅使用按键说话 - 拥有此限制的成员加入语音频道后，只能使用按键说话。
	const val FreeSpeak           = 0b00000010000000000000000000000 // 使用自由麦 - 没有此权限的成员，必须在频道内使用按键说话。
	const val Speak               = 0b00000100000000000000000000000 // 说话
	const val GuildMuteHeadset    = 0b00001000000000000000000000000 // 服务器静音
	const val GuildMuteMicrophone = 0b00010000000000000000000000000 // 服务器静言
	const val ModifyOthersDisplay = 0b00100000000000000000000000000 // 修改他人昵称
	const val PlayBackgroundMusic = 0b01000000000000000000000000000 // 播放伴奏

}