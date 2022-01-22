package kaikt.api.entity.request

class GuildOrChannel private constructor(val guildId: String? = null, val channelId: String? = null) {

	class Builder {

		private var guildId: String? = null
		private var channelId: String? = null

		fun setGuildId(roleId: String) = apply { this.guildId = roleId }
		fun setChannelId(userId: String) = apply { this.channelId = userId }

		fun build(): GuildOrChannel {
			if(guildId == null && channelId == null) {
				throw Exception("Either guildId or channelId should not be null.")
			}
			return GuildOrChannel(guildId, channelId)
		}

	}

	companion object {
		/**
		 * 建立服务器身分组的[GuildOrChannel]
		 */
		fun withGuildId(guildId: String) = Builder().setGuildId(guildId).build()

		/**
		 * 建立用户的[GuildOrChannel]
		 */
		fun withChannelId(channelId: String) = Builder().setChannelId(channelId).build()
	}

	fun hasGuildId() = guildId != null
	fun hasChannelId() = channelId != null

	val type
		get() =
			if(hasGuildId()) {
				"guild_id"
			} else {
				"channel_id"
			}

	val value
		get() =
			if(hasGuildId()) {
				guildId
			} else {
				channelId
			}
}