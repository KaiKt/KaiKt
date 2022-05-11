package kaikt

import kaikt.api.util.uploadAsset
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

	@Test
	fun downloadExternal() {
		val url = "https://www.bungie.net/common/destiny2_content/icons/ee96fc4754ef4a1427994dce6e5dcf3c.jpg"
		uploadAsset(url)
	}

	@Test
	fun testReUpload() {
		val url = "https://www.bungie.net/common/destiny2_content/icons/ee96fc4754ef4a1427994dce6e5dcf3c.jpg"
		println(api.Asset().uploadExternal(url))
	}

}