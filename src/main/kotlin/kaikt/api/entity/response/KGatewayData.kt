package kaikt.api.entity.response

import java.net.URI

data class KGatewayData(val url: String) {

	fun getURI() = URI(url)
}