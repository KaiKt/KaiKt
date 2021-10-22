package kaikt.websocket.acorn.v0

import kaikt.websocket.acorn.*

class AcornGuildBuilderImpl(acorn: AcornFactory) : AcornGuildBuilder(acorn) {

	override fun create(): AcornGuild {
		class AcornGuildImpl : AcornGuild {

			private val userData by lazy {
				acorn.getFactoryApi().Guild().getGuildUserList(guildId).throwIfNotSuccess().data
			}

			private val guildData by lazy {
				acorn.getFactoryApi().Guild().getGuildView(guildId).throwIfNotSuccess().data
			}

			private val userList by lazy {
				userData.items.map {
					acorn.buildAcornUser {
						this.userId = it.id
					}
				}
			}

			private val channelList by lazy {
				guildData.channels.map {
					acorn.buildAcornChannel {
						this.channelId = it.id
						this.guildId = it.guildId
					}
				}
			}

			override fun getName(): String {
				return guildData.name
			}

			override fun getUsers(): List<AcornUser> {
				return userList
			}

			override fun getChannels(): List<AcornChannel> {
				return channelList
			}

		}
		return AcornGuildImpl()
	}
}