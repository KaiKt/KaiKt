package kaikt.cardmsg

import kaikt.cardmsg.entity.Element

/**
 * 图片模块大图集
 */
class ContainerComponent(val elements: List<Element>): CardComponent("container") {
	constructor(element: Element): this(listOf(element))
}