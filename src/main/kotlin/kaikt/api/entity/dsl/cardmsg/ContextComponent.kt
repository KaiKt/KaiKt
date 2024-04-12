package kaikt.api.entity.dsl.cardmsg

import kaikt.api.entity.dsl.cardmsg.entity.Element
import kaikt.api.entity.dsl.cardmsg.entity.ImageElement
import kaikt.api.entity.dsl.cardmsg.entity.TextKMarkdown
import kaikt.api.entity.dsl.cardmsg.entity.TextPlainText

class ContextComponent(private val elements: MutableList<Element> = mutableListOf()): CardComponent("context") {
	fun addContent(element: Element) {
		if(element is TextPlainText || element is TextKMarkdown || element is ImageElement) {
			elements += element
		}
	}
}