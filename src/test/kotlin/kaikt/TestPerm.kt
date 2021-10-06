package kaikt

import kaikt.api.entity.permission.KPermissionBits
import org.junit.jupiter.api.Test

class TestPerm {

	@Test
	fun testPerm() {

		KPermissionBits::class.java.declaredFields.forEach {
			println("${it.name} \t ${it.get(KPermissionBits)}")
		}

		println(
			"Is Admin = ${KPermissionBits.Administrator.and(KPermissionBits.Administrator)}"
		)

	}

}