package kaikt.api.entity.dsl.cardmsg

import kaikt.api.entity.dsl.cardmsg.entity.Text
import kaikt.api.entity.dsl.cardmsg.entity.TextPlainText

/**
 * 内容模块（单文本型）
 */
open class SectionComponent(val text: Text): CardComponent("section")

/**
 * 内容模块（文本+图片/按钮）
 */
class SectionComponentTextAndAccessory(text: Text, val accessory: SectionAccessory, val mode: String = "right"):
	SectionComponent(text)

open class SectionAccessory internal constructor(val type: String)

/**
 * [size] 可用 "lg" 或 "sm"
 */
class SectionAccessoryImage(val src: String, val size: String = "lg"): SectionAccessory("image")
class SectionAccessoryButton(val theme: String = "secondary", val text: TextPlainText): SectionAccessory("button") {
	constructor(theme: String, content: String): this(theme, TextPlainText(content))
}
