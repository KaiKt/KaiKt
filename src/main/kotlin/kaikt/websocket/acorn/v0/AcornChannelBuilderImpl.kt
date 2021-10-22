package kaikt.websocket.acorn.v0

import kaikt.websocket.acorn.*
import kaikt.websocket.acorn.entity.AcornMessageRequest

/**
 * [AcornChannel] 构造器，必须为[channelId]赋值！
 */
class AcornChannelBuilderImpl(acorn: AcornFactory) : AcornChannelBuilder(acorn) {

	val data by lazy {
		acorn.getFactoryApi()
			.Channel().getChannelView(channelId).throwIfNotSuccess().data
	}

	override fun create(): AcornChannel {
		class AcornChannelImpl : AcornChannel {

			override fun getGuild(): AcornGuild {
				return acorn.buildAcornGuild {
					this.guildId = data.guildId
				}
			}

			override fun getChannelType(): Int {
				return data.type
			}

			override fun isCategory(): Boolean {
				return data.isCategory
			}

			override fun getId(): String {
				return this@AcornChannelBuilderImpl.channelId
			}

			override fun sendMessage(content: String, request: AcornMessageRequest.() -> Unit): AcornMessage {
				val reqData = AcornMessageRequest(this, content).apply(request)
				val created = acorn.getFactoryApi()
					.Message().postMessageCreate(getId(), content, reqData::writeData).throwIfNotSuccess().data
				return acorn.buildAcornMessage {
					this.messageId = created.msgId
					this.source = this@AcornChannelImpl
					this.messageContent = content
					this.messageTimestamp = created.msgTimestamp
				}
			}
		}
		return AcornChannelImpl()
	}
}