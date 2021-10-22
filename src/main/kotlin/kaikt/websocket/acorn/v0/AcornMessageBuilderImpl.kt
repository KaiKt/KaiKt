package kaikt.websocket.acorn.v0

import kaikt.api.entity.request.*
import kaikt.websocket.acorn.*
import kaikt.websocket.acorn.entity.AcornMessageRequest

class AcornMessageBuilderImpl(acorn: AcornFactory) : AcornMessageBuilder(acorn) {

	override fun create(): AcornMessage {

		class AcornMessageImpl: AcornMessage {

			// 当数据有缺失的时候被调用
			val data by lazy {
				if(this@AcornMessageBuilderImpl.source is AcornChannel) { // 服务器频道
					val req = acorn.getFactoryApi().Message().getMessageList(source.getId()) {
						flag = QueryFlag.Around
						msgId = this@AcornMessageBuilderImpl.messageId
						pageSize = 1
					}.throwIfNotSuccess()
					return@lazy req.data.items.first()
				} else { // 用户私聊
					val req = acorn.getFactoryApi()
						.DirectMessage().getDirectMessageList(TargetIdOrChatCode.withTargetId(source.getId())) {
							flag = QueryFlag.Around
							msgId = this@AcornMessageBuilderImpl.messageId
							pageSize = 1
						}.throwIfNotSuccess()
					return@lazy req.data.items.first()
				}
			}

			override fun getId(): String {
				return messageId
			}

			override fun getMessage(): String {
				return messageContent ?: data.content
			}

			override fun getMessageTimestamp(): Long {
				return messageTimestamp ?: data.createdAt
			}

			override fun reply(content: String, request: AcornMessageRequest.() -> Unit): AcornMessage {
				// 请求发送的消息的具体内容（KMessageRequest）
				val reqData = AcornMessageRequest(this@AcornMessageBuilderImpl.source, content).apply(request)

				// 请求发送消息，并获取返回的信息，若未成功则直接抛出错误
				val created = if(this@AcornMessageBuilderImpl.source is AcornChannel) { // Channel Mode
					acorn.getFactoryApi()
						.Message().postMessageCreate(source.getId(), content, reqData::writeData)
						.throwIfNotSuccess().data
				} else {
					acorn.getFactoryApi()
						.DirectMessage().postDirectMessageCreate(
							TargetIdOrChatCode.withTargetId(source.getId()),
							content,
							reqData::writeData
						).throwIfNotSuccess().data
				}

				// 将返回的消息信息打包
				return acorn.buildAcornMessage {
					this.messageId = created.msgId
					this.source = this@AcornMessageBuilderImpl.source
					this.messageContent = content
					this.messageTimestamp = created.msgTimestamp
				}
			}

			override fun addReaction(emojiId: String): Boolean {
				val result = if(this@AcornMessageBuilderImpl.source is AcornChannel) {
					acorn.getFactoryApi().Message().postMessageAddReaction(messageId, emojiId)
				} else {
					acorn.getFactoryApi().DirectMessage().postDirectMessageAddReaction(messageId, emojiId)
				}
				return result.isSuccess()
			}

			override fun delReaction(emojiId: String): Boolean {
				val result = if(this@AcornMessageBuilderImpl.source is AcornChannel) {
					acorn.getFactoryApi().Message().postMessageDeleteReaction(messageId, emojiId)
				} else {
					acorn.getFactoryApi().DirectMessage().postDirectMessageDeleteReaction(messageId, emojiId)
				}
				return result.isSuccess()
			}
		}

		return AcornMessageImpl()

	}
}