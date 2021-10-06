package kaikt.api.entity.definition

data class KPermissionUserDefinition(val user: KUserDefinition, val allow: Int, val deny: Int)