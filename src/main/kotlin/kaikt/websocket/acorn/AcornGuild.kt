package kaikt.websocket.acorn

interface AcornGuild {

	fun getName(): String

	fun getUsers(): List<AcornUser>

	fun getChannels(): List<AcornChannel>

	fun getChannel(predicate: (AcornChannel) -> Boolean) = getChannels().first(predicate)

}