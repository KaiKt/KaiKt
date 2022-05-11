package kaikt.cardmsg

import kaikt.cardmsg.entity.ImageElement

class ImageGroupComponent(val elements: MutableList<ImageElement> = mutableListOf()): CardComponent("image-group")