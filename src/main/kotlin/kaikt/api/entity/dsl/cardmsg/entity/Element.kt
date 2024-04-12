package kaikt.api.entity.dsl.cardmsg.entity

import kaikt.api.util.uploadAsset

open class Element internal constructor(val type: String)

@Deprecated("使用 ImageElement 代替")
typealias ElementImage = ImageElement

class ImageElement(var src: String) : Element("image") {
	init {
		src = uploadAsset(src)
	}
}

class ButtonElement(
	val theme: String, /* valid values: primary, secondary, danger */
	val value: String,
	val text: TextPlainText
)