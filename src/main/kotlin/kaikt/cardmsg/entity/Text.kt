package kaikt.cardmsg.entity

open class Text internal constructor(type: String): Element(type)

class TextPlainText(val content: String): Text("plain-text")
class TextKMarkdown(val content: String): Text("kmarkdown")

class TextParagraph(val cols: Int, private val fields: MutableList<Text> = mutableListOf()): Text("paragraph") {
	fun addContent(content: Text) {
		if(content is TextPlainText || content is TextKMarkdown) {
			fields += content
		}
	}
}