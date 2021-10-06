package kaikt.api.entity.request

data class KGuildUserListRequest(val guildId: String) {

	/**
	 * 频道ID
	 */
	var channelId: String? = null

	/**
	 * 搜索关键字，在用户名或昵称中搜索
	 */
	var search: String? = null

	/**
	 * 角色ID，获取特定用户列表
	 */
	var roleId: Int? = null

	/**
	 * 手机认证；0-未认证，1-认证
	 */
	var mobileVerified: Int? = null

	/**
	 * 根据活跃时间排序；0-顺序，1-倒序
	 */
	var activeTime: Int? = null

	/**
	 * 根据加入时间排序；0-顺序，1-倒序
	 */
	var joinedAt: Int? = null

	/**
	 * 目标页数
	 */
	var page: Int? = null

	/**
	 * 每页数据数量
	 */
	var pageSize: Int? = null

	/**
	 * 获取指定ID所属的用户信息
	 */
	var filterUserId: String? = null

	fun toQueryMap(): Map<String, String> {
		return mapOf(
			"guild_id" to guildId,
			"channel_id" to channelId,
			"search" to search,
			"role_id" to roleId,
			"mobile_verified" to mobileVerified,
			"active_time" to activeTime,
			"joined_at" to joinedAt,
			"page" to page,
			"page_size" to pageSize,
			"filter_user_id" to filterUserId
		).filterValues { it != null }.mapValues { it.value.toString() }
	}
}