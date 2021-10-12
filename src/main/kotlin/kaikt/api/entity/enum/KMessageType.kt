package kaikt.api.entity.enum

enum class KMessageType(val i: Int) {

	Text(1),
	Picture(2),
	Video(3),
	File(4),
	KMarkdown(9),
	CardMessage(10);

	fun Int.enumMessageType(): KMessageType {
		values().forEach {
			if(it.i == this) {
				return it
			}
		}
		throw NoSuchElementException("KMessageType value of $i")
	}

}

fun Int.toMessageType() = KMessageType.values().firstOrNull { it.i == this }