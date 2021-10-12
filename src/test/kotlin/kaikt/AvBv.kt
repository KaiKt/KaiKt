package kaikt

import java.util.HashMap
import kotlin.jvm.JvmStatic
import kaikt.AvBv
import java.lang.StringBuilder

object AvBv {
	private const val table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF"
	private val mp = HashMap<String, Int>()
	private val mp2 = HashMap<Int, String>()
	var ss = intArrayOf(
		11, 10, 3, 8, 4, 6, 2, 9, 5, 7
	)
	var xor: Long = 177451812
	var add = 8728348608L
	@JvmStatic
	fun main(args: Array<String>) {
		println(b2v("BV1FE411c7co"))
		println(v2b("av98464354"))
	}

	fun power(a: Int, b: Int): Long {
		var power: Long = 1
		for(c in 0 until b) power *= a.toLong()
		return power
	}

	fun b2v(s: String): String {
		var r: Long = 0
		for(i in 0..57) {
			val s1 = table.substring(i, i + 1)
			mp[s1] = i
		}
		for(i in 0..5) {
			r += mp[s.substring(ss[i], ss[i] + 1)]!! * power(58, i)
		}
		return "av" + (r - add xor xor)
	}

	fun v2b(st: String): String {
		var s = st.split("av".toRegex()).toTypedArray()[1].toLong()
		val sb = StringBuilder("BV1  4 1 7  ")
		s = (s xor xor) + add
		for(i in 0..57) {
			val s1 = table.substring(i, i + 1)
			mp2[i] = s1
		}
		for(i in 0..5) {
			val r = mp2[(s / power(58, i) % 58).toInt()]
			sb.replace(ss[i], ss[i] + 1, r)
		}
		return sb.toString()
	}
}