package kaikt.api

class KToken(private val type: TokenType, private val token: String) {

	enum class TokenType {
		Bot, OAuth;

		override fun toString(): String {
			return when(this) {
				Bot -> "Bot"
				OAuth -> "Bearer"
			}
		}
	}

	override fun toString() = "$type ${token.substring(0, 3)}*****${token.substring(token.length-4)}"

	fun toFullToken() = "$type $token"

	fun toApi() = KaiApi(this)

}
