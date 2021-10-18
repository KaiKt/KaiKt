package kaikt.cardmsg

import kaikt.cardmsg.entity.ElementImage

class ImageGroupComponent(val elements: MutableList<ElementImage> = mutableListOf()): CardComponent("image-group")