package kaikt

import org.junit.jupiter.api.Test
import java.io.File

class TestMediaUpload {

	@Test
	fun testMediaUpload() {

		val picFile = File("./src/test/resources/OfAllTradesGuide.png")
		if(picFile.exists()) {
			api.Asset().postAssetCreate(picFile).let {
				if(it.isSuccess()) {
					println("Successfully Uploaded!")
					println(it.data)
				} else {
					println("Cannot Upload the Asset!")
					println(it)
				}
			}
		} else {
			println("Error! File missing!")
			File("./src/test/resources/").listFiles()?.forEach {
				println(it)
			}
		}

	}

}