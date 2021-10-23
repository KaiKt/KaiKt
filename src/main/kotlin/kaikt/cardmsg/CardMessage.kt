package kaikt.cardmsg

import com.google.gson.reflect.TypeToken
import kaikt.cardmsg.entity.*
import kaikt.gson

class CardMessage(
	var theme: String = "secondary",
	var size: String = "lg",
	private val modules: MutableList<CardComponent> = mutableListOf()
) : CardComponent("card") {

	fun addComponent(module: CardComponent) {
		if(module !is CardMessage) {
			modules += module
		}
	}

	fun toJson(): String = mutableListOf(this).toJson()

}

typealias CardMessages = MutableList<CardMessage>

fun CardMessages.toJson(): String = gson.toJson(this, (object : TypeToken<CardMessages>() {}).type)

fun buildCardMessages(block: CardMessages.() -> Unit): String {
	return mutableListOf<CardMessage>().apply(block).toJson()
}

fun CardMessages.card(block: CardMessage.() -> Unit) =
	apply { add(CardMessage().apply(block)) }

fun buildCardMessage(block: CardMessage.() -> Unit): String {
	return CardMessage().apply(block).toJson()
}

fun CardMessage.text(content: String) = addComponent(SectionComponent(TextPlainText(content)))
fun CardMessage.kMarkdown(content: String) = addComponent(SectionComponent(TextKMarkdown(content)))
fun CardMessage.paragraph(cols: Int, block: TextParagraph.() -> Unit) =
	addComponent(SectionComponent(TextParagraph(cols).apply(block)))
fun CardMessage.textAndImage(content: String, imageSrc: String, size: String = "lg") =
	addComponent(SectionComponentTextAndAccessory(TextPlainText(content), SectionAccessoryImage(imageSrc, size)))
fun CardMessage.textAndButton(content: String, btnContent: String, theme: String = "primary") =
	addComponent(SectionComponentTextAndAccessory(TextPlainText(content), SectionAccessoryButton(theme, btnContent)))
fun CardMessage.image(imageSrc: String) =
	addComponent(ContainerComponent(ElementImage(imageSrc)))
fun CardMessage.imageGroup(block: ImageGroupComponent.() -> Unit) =
	addComponent(ImageGroupComponent().apply(block))
fun CardMessage.header(content: String) =
	addComponent(HeaderComponent(TextPlainText(content)))
fun CardMessage.divider() =
	addComponent(DividerComponent())
fun CardMessage.actionGroup(block: ActionGroupComponent.() -> Unit) =
	addComponent(ActionGroupComponent().apply(block))
fun CardMessage.context(block: ContextComponent.() -> Unit) =
	addComponent(ContextComponent().apply(block))
fun CardMessage.file(title: String, fileSrc: String, size: String) =
	addComponent(FileComponent(title, fileSrc, size))
fun CardMessage.audio(title: String, audioSrc: String, cover: String) =
	addComponent(AudioComponent(title, audioSrc, cover))
fun CardMessage.video(title: String, videoSrc: String) =
	addComponent(VideoComponent(title, videoSrc))

fun CardMessage.cdDay(endTime: Long) =
	addComponent(CountdownDay(endTime))
fun CardMessage.cdHour(endTime: Long) =
	addComponent(CountdownHour(endTime))
fun CardMessage.cdSec(startTime: Long, endTime: Long) =
	addComponent(CountdownSecond(startTime, endTime))

// TODO: 2021/10/15 更多 CD 相关方法

fun TextParagraph.text(content: String) = addContent(TextPlainText(content))
fun TextParagraph.kMarkdown(content: String) = addContent(TextKMarkdown(content))

fun ImageGroupComponent.image(imageSrc: String) =
	apply { elements += ElementImage(imageSrc) }

fun ActionGroupComponent.button(theme: String, value: String, content: String) =
	apply { elements += ButtonElement(theme, value, TextPlainText(content)) }

fun ContextComponent.text(content: String) =
	addContent(TextPlainText(content))
fun ContextComponent.kMarkdown(content: String) =
	addContent(TextKMarkdown(content))
fun ContextComponent.image(imageSrc: String) =
	addContent(ElementImage(imageSrc))