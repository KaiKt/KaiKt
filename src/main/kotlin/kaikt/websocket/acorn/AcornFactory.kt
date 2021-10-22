package kaikt.websocket.acorn

import kaikt.api.KaiApi

interface AcornFactory {

	fun getFactoryApi(): KaiApi

	fun createAcornUser(userId: String, block: AcornUserBuilder.() -> Unit = {}): AcornUser

	fun createAcornGuild(guildId: String, block: AcornGuildBuilder.() -> Unit = {}): AcornGuild

	fun createAcornChannel(channelId: String, block: AcornChannelBuilder.() -> Unit = {}): AcornChannel

	fun createAcornMessage(messageId: String, source: AcornMessageSource, block: AcornMessageBuilder.() -> Unit = {}): AcornMessage

	fun buildAcornUser(block: AcornUserBuilder.() -> Unit): AcornUser

	fun buildAcornGuild(block: AcornGuildBuilder.() -> Unit): AcornGuild

	fun buildAcornChannel(block: AcornChannelBuilder.() -> Unit): AcornChannel

	fun buildAcornMessage(block: AcornMessageBuilder.() -> Unit): AcornMessage

}