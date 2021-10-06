package kaikt.api.entity.request

class TargetIdOrChatCode private constructor(val targetId: String? = null, val chatCode: String? = null) {

	class Builder {

		private var targetId: String? = null
		private var chatCode: String? = null

		fun setTargetId(targetId: String) = apply { this.targetId = targetId }
		fun setChatCode(chatCode: String) = apply { this.chatCode = chatCode }

		fun build(): TargetIdOrChatCode {
			if(targetId == null && chatCode == null) {
				throw Exception("Either targetId or chatCode should not be null.")
			}
			return TargetIdOrChatCode(targetId, chatCode)
		}

	}

	companion object {
		fun withTargetId(targetId: String) = Builder().setTargetId(targetId).build()
		fun withChatCode(chatCode: String) = Builder().setChatCode(chatCode).build()
	}

}