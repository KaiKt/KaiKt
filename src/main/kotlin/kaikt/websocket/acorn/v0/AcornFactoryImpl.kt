package kaikt.websocket.acorn.v0

import kaikt.api.KaiApi
import kaikt.websocket.KaiClient
import kaikt.websocket.acorn.*

class AcornFactoryImpl(val client: KaiClient) : AcornFactory {

	override fun getFactoryApi(): KaiApi {
		return client.api
	}

	override fun createAcornUser(userId: String, block: AcornUserBuilder.() -> Unit): AcornUser {
		return AcornUserBuilderImpl(this).apply(block).apply {
			this.userId = userId
		}.create()
	}

	override fun createAcornGuild(guildId: String, block: AcornGuildBuilder.() -> Unit): AcornGuild {
		return AcornGuildBuilderImpl(this).apply(block).apply {
			this.guildId = guildId
		}.create()
	}

	override fun createAcornChannel(channelId: String, block: AcornChannelBuilder.() -> Unit): AcornChannel {
		return AcornChannelBuilderImpl(this).apply(block).apply {
			this.channelId = channelId
		}.create()
	}

	override fun createAcornMessage(messageId: String, source: AcornMessageSource, block: AcornMessageBuilder.() -> Unit): AcornMessage {
		return AcornMessageBuilderImpl(this).apply(block).apply {
			this.messageId = messageId
			this.source = source
		}.create()
	}

	override fun buildAcornUser(block: AcornUserBuilder.() -> Unit): AcornUser {
		return AcornUserBuilderImpl(this).apply(block).create()
	}

	override fun buildAcornGuild(block: AcornGuildBuilder.() -> Unit): AcornGuild {
		return AcornGuildBuilderImpl(this).apply(block).create()
	}

	override fun buildAcornChannel(block: AcornChannelBuilder.() -> Unit): AcornChannel {
		return AcornChannelBuilderImpl(this).apply(block).create()
	}

	override fun buildAcornMessage(block: AcornMessageBuilder.() -> Unit): AcornMessage {
		return AcornMessageBuilderImpl(this).apply(block).create()
	}

}