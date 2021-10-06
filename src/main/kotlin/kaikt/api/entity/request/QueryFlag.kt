package kaikt.api.entity.request

enum class QueryFlag(val serializedName: String) {
	/**
	 * 查询参考消息之前的消息，不包括参考消息
	 */
	Before("before"),

	/**
	 * 查询以参考消息为中心，前后一定数量的消息
	 */
	Around("around"),

	/**
	 * 查询参考消息之后的消息，不包括参考消息
	 */
	After("after")
}