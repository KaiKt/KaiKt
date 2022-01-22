package kaikt.api.entity.request

enum class BadgeStyle(val value: Int) {

	/**
	 * 服务器名称
	 */
	GUILD_NAME(0),

	/**
	 * 服务器在线人数
	 */
	GUILD_ONLINE_COUNT(1),

	/**
	 * 服务器在线人数比服务器所有人数
	 */
	GUILD_ONLINE_ALL_COUNT(2)

}