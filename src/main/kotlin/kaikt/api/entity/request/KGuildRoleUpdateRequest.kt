package kaikt.api.entity.request

import kaikt.api.util.valueNotNullMapOf

import kaikt.api.entity.permission.KPermission

class KGuildRoleUpdateRequest(val guildId: String, val roleId: String) {

	/**
	 * 身分组名称
	 */
	var name: String? = null

	/**
	 * 身分组颜色（16进制，例如：0x66FFCC）
	 */
	var color: String? = null

	/**
	 * 是否突出显示身分组（即在用户列表内单独成组）
	 */
	var hoist: String? = null

	/**
	 * 是否可以被提及（@）
	 */
	var mentionable: String? = null

	/**
	 * [身分组权限][KPermission]
	 */
	var permissions: String? = null

	fun setHoist(hoist: Boolean) = apply { this.hoist = if(hoist) "1" else "0" }
	fun setMentionable(mentionable: Boolean) = apply { this.mentionable = if(mentionable) "1" else "0" }

	fun setPermission(perm: Int) = apply { this.permissions = perm.toString() }
	fun setPermission(perm: UInt) = setPermission(perm.toInt())
	fun setPermission(perm: Long) = setPermission(perm.toUInt())

	val body
		get() = valueNotNullMapOf(
			"guild_id" to guildId,
			"role_id" to roleId,
			"name" to name,
			"color" to color,
			"hoist" to hoist,
			"mentionable" to mentionable,
			"permissions" to permissions
		)

}