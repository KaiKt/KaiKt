package kaikt

import kaikt.api.entity.permission.*
import org.junit.jupiter.api.Test

class TestPerm {

	@Test
	fun testPerm() {

		val p = KPermission(0u)

		println(p)
		p.grant(PermissionEnum.Administrator)
		println(p)
		p.grant(PermissionEnum.BlockUser)
		println(p)

		println(p.has(PermissionEnum.BlockUser)) // true
		println(p.has(PermissionEnum.KickUser)) // true

		p.revoke(PermissionEnum.Administrator)
		println(p)

		println(p.has(PermissionEnum.KickUser)) // false

	}

}