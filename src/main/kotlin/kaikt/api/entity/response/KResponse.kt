package kaikt.api.entity.response

data class KResponse<T>(val code: Int, val message: String, val data: T) {

	fun isSuccess() = code == 0

}

typealias KBasicResponse = KResponse<Any>