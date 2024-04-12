package kaikt.api.entity.dsl.cardmsg

import kaikt.api.entity.dsl.cardmsg.entity.ButtonElement

class ActionGroupComponent(val elements: MutableList<ButtonElement> = mutableListOf()) : CardComponent("action-group")