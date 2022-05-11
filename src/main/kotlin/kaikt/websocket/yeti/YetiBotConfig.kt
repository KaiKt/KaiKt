package kaikt.websocket.yeti

class YetiBotConfig(val token: String, val loggerName: String) {

	class Builder {

		var token: String? = null
		var loggerName: String? = "Yeti"

		fun build(): YetiBotConfig {
			return YetiBotConfig(token!!, loggerName!!)
		}
	}

}