package kaikt.websocket.acorn

abstract class AcornUserBuilder(val acorn: AcornFactory) {

	lateinit var userId: String

	var guildId: String? = null

	abstract fun create(): AcornUser
}

abstract class AcornGuildBuilder(val acorn: AcornFactory) {

	lateinit var guildId: String

	abstract fun create(): AcornGuild
}

abstract class AcornChannelBuilder(val acorn: AcornFactory) {

	lateinit var channelId: String

	var guildId: String? = null

	abstract fun create(): AcornChannel
}

abstract class AcornMessageBuilder(val acorn: AcornFactory) {

	lateinit var messageId: String
	lateinit var source: AcornMessageSource

	var messageContent: String? = null
	var messageTimestamp: Long? = null

	abstract fun create(): AcornMessage
}