package kaikt.websocket.acorn

interface AcornChannel: AcornMessageSource {

	fun getGuild(): AcornGuild

	fun getChannelType(): Int

	fun isCategory(): Boolean

	fun isTextChannel(): Boolean {
		return getChannelType() == 1
	}

	fun isVoiceChannel(): Boolean {
		return getChannelType() == 2
	}
}