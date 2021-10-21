package kaikt.api.util

class IntBool(var value: Boolean) {
	override fun toString(): String = "${this.toInt()}b"
}

fun IntBool.toInt() = if(this.value) 1 else 0
fun IntBool.toBoolean() = this.value

fun Int.toIntBool() = IntBool(this == 1)
fun Boolean.toIntBool() = IntBool(this)
fun String.toIntBool() =
	this.toIntOrNull()?.toIntBool() ?:
	this.toBooleanStrictOrNull()?.toIntBool() ?:
	throw IllegalStateException("cannot cast $this to IntBool")