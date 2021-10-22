package kaikt.websocket.acorn

interface AcornUser: AcornMessageSource {

	fun getName(): String

	fun getNickname(): String?

	fun isBot(): Boolean?

}