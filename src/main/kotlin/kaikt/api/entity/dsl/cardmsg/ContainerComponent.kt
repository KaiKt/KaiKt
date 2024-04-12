package kaikt.api.entity.dsl.cardmsg

import kaikt.api.entity.dsl.cardmsg.entity.Element

/**
 * 图片模块大图集
 */
class ContainerComponent(val elements: List<Element>): CardComponent("container") {
	constructor(element: Element): this(listOf(element))
}