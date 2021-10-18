package kaikt.cardmsg

import kaikt.cardmsg.entity.ButtonElement

class ActionGroupComponent(val elements: MutableList<ButtonElement> = mutableListOf()) : CardComponent("action-group")