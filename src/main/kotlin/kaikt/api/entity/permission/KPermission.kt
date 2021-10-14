package kaikt.api.entity.permission

class KPermission(var permission: UInt) {

	/*
	 * 使用方法：
	 * val p = 3213213213321; // 目前权限，查询是否有 Administrator
	 * val isAdmin = (p.and(KPermission.Administrator) != 0)
	 */

	private fun has0(perm: PermissionEnum): Boolean {
		return this.permission or perm.toUInt() == this.permission
	}

	fun isAdmin(): Boolean {
		return has0(PermissionEnum.Administrator)
	}

	fun has(perm: PermissionEnum): Boolean {
		return has0(perm) || isAdmin()
	}

	fun grant(perm: PermissionEnum) {
		this.permission = this.permission or perm.toUInt()
	}

	fun revoke(perm: PermissionEnum) {
		this.permission = this.permission and (perm.toUInt().inv())
	}

	override fun equals(other: Any?): Boolean {
		return if(other is KPermission) {
			this.permission == other.permission
		} else {
			false
		}
	}

	override fun hashCode(): Int {
		return this.permission.toInt()
	}

	override fun toString(): String {
		return this.permission.toString(2)
	}

}

enum class PermissionEnum(val bit: Int) {

	Administrator(0),

	/**
	 * 管理服务器
	 */
	ManageGuild(1),

	/**
	 * 管理服务器日志
	 */
	ManageGuildLogging(2),

	/**
	 * 创建邀请连接
	 */
	CreateInvitation(3),

	/**
	 * 管理邀请
	 */
	ManageInvitation(4),

	/**
	 * 管理频道
	 */
	ManageChannel(5),

	/**
	 * 踢出服务器成员
	 */
	KickUser(6),

	/**
	 * 禁封服务器成员
	 */
	BlockUser(7),

	/**
	 * 管理服务器表情
	 */
	ManageGuildEmoji(8),

	/**
	 * 改变昵称
	 */
	ChangeDisplayName(9),

	/**
	 * 管理身分组
	 */
	ManageRole(10),

	/**
	 * 能否看到频道
	 */
	SeeChannel(11),

	/**
	 * 发送消息
	 */
	SendMessage(12),

	/**
	 * 管理消息
	 */
	ManageMessage(13),

	/**
	 * 上传文件
	 */
	UploadFiles(14),

	/**
	 * 连接语音频道
	 */
	ConnectVoiceChannel(15),

	/**
	 * 管理语音频道
	 */
	ManageVoiceChannel(16),

	/**
	 * 能否 @全体成员
	 */
	MentionAll(17),

	/**
	 * 添加一个新的回应
	 */
	AddNewReaction(18),

	/**
	 * 跟随一个已有的回应
	 */
	AddExistReaction(19),

	/**
	 * 被动连接语音 - 拥有此限制的成员无法主动连接语音频道，只能在被动邀请或被人移动时，才可以进入语音频道。
	 */
	PassivelyVoiceConnect(20),

	/**
	 * 仅允许按键说话 - 拥有此限制的成员加入语音频道后，只能使用按键说话。
	 */
	PressToSpeakOnly(21),

	/**
	 * 使用自由麦 - 没有此权限的成员，必须在频道内使用按键说话。
	 */
	FreeSpeak(22),

	/**
	 * 说话
	 */
	Speak(23),

	/**
	 * 服务器静音
	 */
	GuildMuteHeadset(24),

	/**
	 * 服务器静言
	 */
	GuildMuteMicrophone(25),

	/**
	 * 管理服务器其他用户的昵称
	 */
	ChangeUserDisplayName(26),

	/**
	 * 播放背景音乐
	 */
	PlayBackgroundMusic(27)
	;

	fun toUInt(): UInt {
		return 1U.shl(bit)
	}

}

@Deprecated("使用 [PermissionEnum]。")
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