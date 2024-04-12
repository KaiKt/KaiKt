package kaikt.api.entity.dsl.cardmsg

import kaikt.api.entity.dsl.cardmsg.entity.ImageElement

class ImageGroupComponent(val elements: MutableList<ImageElement> = mutableListOf()): CardComponent("image-group")