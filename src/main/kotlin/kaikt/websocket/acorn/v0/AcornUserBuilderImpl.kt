package kaikt.websocket.acorn.v0

import kaikt.api.entity.request.TargetIdOrChatCode
import kaikt.websocket.acorn.*
import kaikt.websocket.acorn.entity.AcornMessageRequest

class AcornUserBuilderImpl(acorn: AcornFactory) : AcornUserBuilder(acorn) {

	override fun create(): AcornUser {
		val data by lazy { acorn.getFactoryApi().User().getUserView(userId, guildId).throwIfNotSuccess().data }

		class AcornUserImpl: AcornUser {

			override fun getName(): String {
				return data.username
			}

			override fun getNickname(): String {
				return data.nickname
			}

			override fun isBot(): Boolean {
				return data.bot
			}

			override fun getId(): String {
				return userId
			}

			override fun sendMessage(content: String, request: AcornMessageRequest.() -> Unit): AcornMessage {
				val reqData = AcornMessageRequest(this, content).apply(request)
				val created = acorn.getFactoryApi().DirectMessage().postDirectMessageCreate(TargetIdOrChatCode.withTargetId(userId), content, reqData::writeData).throwIfNotSuccess().data
				return acorn.buildAcornMessage {
					this.messageId = created.msgId
					this.source = this@AcornUserImpl
					this.messageContent = content
					this.messageTimestamp = created.msgTimestamp
				}
			}
		}
		return AcornUserImpl()
	}
}