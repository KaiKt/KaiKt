package kaikt.api.entity.response

import com.google.gson.annotations.SerializedName

data class KResponse<T>(val code: Int, val message: String, val data: T) {

	fun isSuccess() = code == 0

}

data class KListData<T>(val items: List<T>, val meta: KMetadata?, val sort: KSortData?)

data class KMetadata(
	val page: Int,
	@SerializedName("page_total") val pageTotal: Int,
	@SerializedName("page_size") val pageSize: Int,
	val total: Int
)

data class KSortData(val id: Int)

typealias KBasicResponse = KResponse<Any>
typealias KListResponse<LT> = KResponse<KListData<LT>>
typealias KBuggedListResponse<LT> = KResponse<List<LT>>