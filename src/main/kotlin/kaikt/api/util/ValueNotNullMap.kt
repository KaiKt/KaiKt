package kaikt.api.util

internal fun <A, B> valueNotNullMapOf(vararg pairs: Pair<A, B?>): Map<A, B> =
	mapOf(*pairs).filterValues { it != null }.mapValues { it.value!! }