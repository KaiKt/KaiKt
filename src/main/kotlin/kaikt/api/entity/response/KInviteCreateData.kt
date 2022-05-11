package kaikt.api.entity.response

data class KInviteCreateData(val url: String) {

	fun getUrlCode() = url.substring(url.lastIndexOf('/')+1)
}