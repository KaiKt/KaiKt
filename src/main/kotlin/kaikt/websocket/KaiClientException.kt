package kaikt.websocket

class KaiClientException(val type: Type, override val message: String, override val cause: Throwable? = null) : RuntimeException() {

	enum class Type {
		WelcomeTimedOut,
		HeartbeatSkip
	}
}