package kaikt.api.entity.request

enum class InviteDuration(val value: Int) {
	/** 永不过期 */
	NEVER(0),
	/** 30分钟 */
	HALF_AN_HOUR(1800),
	/** 1小时 */
	AN_HOUR(3600),
	/** 6小时 */
	SIX_HOURS(21600),
	/** 12小时 */
	HALF_A_DAY(43200),
	/** 24小时 */
	A_DAY(86400),
	/** 7天 */
	A_WEEK(604800)
}

enum class InviteUsageCounter(val value: Int) {
	NEVER(-1),
	ONCE(1),
	TIMES_5(5),
	TIMES_10(10),
	TIMES_25(25),
	TIMES_50(50),
	TIMES_100(100)
}