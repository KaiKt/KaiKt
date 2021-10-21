package kaikt.cardmsg.entity

open class Element internal constructor(val type: String)

class ElementImage(val src: String) : Element("image")
class ButtonElement(
	val theme: String, /* valid values: primary, secondary, danger */
	val value: String,
	val text: TextPlainText
)