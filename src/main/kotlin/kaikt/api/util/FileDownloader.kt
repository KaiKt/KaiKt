package kaikt.api.util

import kaikt.api.KaiApi
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.*

object FileDownloader {

	val client = OkHttpClient()

	private val cacheDir by lazy {
		File("./cache").apply {
			if(!exists()) {
				mkdirs()
			}
		}
	}

	fun download(src: String): File {
		val req = Request.Builder().url(src).get().build()
		val resp = client.newCall(req).execute()
		val reader = resp.body?.bytes()

		val ext = kotlin.run {
			src.indexOfLast { it == '.' }.let {
				if(it == -1) {
					null
				} else {
					src.substring(it)
				}
			}
		}

		if(reader != null) {
			val f = newCacheFile(ext)
			write(f, reader)
			return f
		} else {
			throw IllegalStateException("Failed to upload assets. [$src]")
		}
	}

	private fun newCacheFile(ext: String? = null): File {
		return File(cacheDir, "${UUID.randomUUID()}${if(ext != null) "$ext" else ""}")
	}

	private fun write(file: File, bytes: ByteArray) {
		if(!file.exists()) {
			file.createNewFile()
		}

		file.outputStream().write(bytes)
	}

}

internal fun uploadAsset(src: String, api: KaiApi.Asset = KaiApi.unspecifiedInstance.Asset()): String =
	uploadAsset(FileDownloader.download(src), api)

internal fun uploadAsset(file: File, api: KaiApi.Asset = KaiApi.unspecifiedInstance.Asset()): String {
	assert(!file.exists()) { "File does not exist. [$file]" }
	assert(!file.isFile) { "File does not a file. [$file]" }

	api.postAssetCreate(file).let {
		if(it.isSuccess()) {
			return it.data.url
		} else {
			throw IllegalStateException("Failed to upload assets. [$file]")
		}
	}
}