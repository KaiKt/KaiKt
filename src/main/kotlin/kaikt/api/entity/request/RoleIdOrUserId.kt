package kaikt.api.entity.request

class RoleIdOrUserId private constructor(val roleId: String? = null, val userId: String? = null) {

	class Builder {

		private var roleId: String? = null
		private var userId: String? = null

		fun setRoleId(roleId: String) = apply { this.roleId = roleId }
		fun setUserId(userId: String) = apply { this.userId = userId }

		fun build(): RoleIdOrUserId {
			if(roleId == null && userId == null) {
				throw Exception("Either targetId or chatCode should not be null.")
			}
			return RoleIdOrUserId(roleId, userId)
		}

	}

	companion object {
		/**
		 * 建立服务器身分组的[RoleIdOrUserId]
		 */
		fun withRoleId(roleId: String) = Builder().setRoleId(roleId).build()

		/**
		 * 建立用户的[RoleIdOrUserId]
		 */
		fun withUserId(userId: String) = Builder().setUserId(userId).build()
	}

	fun hasRoleId() = roleId != null
	fun hasUserId() = userId != null

	val type
		get() =
			if(hasRoleId()) {
				"role_id"
			} else {
				"user_id"
			}

	val value
		get() =
			if(hasRoleId()) {
				roleId
			} else {
				userId
			}
}