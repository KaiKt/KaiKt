package kaikt.yeti

import kaikt.websocket.yeti.YetiBot
import kaikt.websocket.yeti.command.*
import org.junit.jupiter.api.Test
import java.lang.reflect.Modifier
import java.util.*

class TestYetiMain {

	@Test
	fun testYeti() {
		YetiBot {
			token = "1/MTA0MjE=/zN5Lh9Na4wZPk0fpeTjDIg=="
		}.apply {
			commandManager.enableListener(object : YetiCommandListener(this) {
				override fun execute(context: MessageContext) {
					println(reflectionToString(context))
					if(context is ChannelMessageContext) {
						val channelView = context.sender.view
						context.sender.sendMessage(channelView.toString())
					} else {
						context.sender.sendMessage("FQ")
					}
				}
			})
		}

		readLine()
	}

	fun reflectionToString(obj: Any): String {
		val s = LinkedList<String>()
		var clazz: Class<in Any>? = obj.javaClass
		while (clazz != null) {
			for (prop in clazz.declaredFields.filterNot { Modifier.isStatic(it.modifiers) }) {
				prop.isAccessible = true
				s += "${prop.name}=" + prop.get(obj)?.toString()?.trim()
			}
			clazz = clazz.superclass
		}
		return "${obj.javaClass.simpleName}=[${s.joinToString(", ")}]"
	}

}