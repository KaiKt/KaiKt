package kaikt.cardmsg

import kaikt.cardmsg.entity.*

class ContextComponent(private val elements: MutableList<Element> = mutableListOf()): CardComponent("context") {
	fun addContent(element: Element) {
		if(element is TextPlainText || element is TextKMarkdown || element is ElementImage) {
			elements += element
		}
	}
}