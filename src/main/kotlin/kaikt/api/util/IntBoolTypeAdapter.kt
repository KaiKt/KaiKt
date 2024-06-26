package kaikt.api.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.*

class IntBoolTypeAdapter : TypeAdapter<IntBool>() {

	override fun write(writer: JsonWriter, value: IntBool) {
		writer.value(value.toBoolean())
	}

	override fun read(reader: JsonReader): IntBool? {
		val peek = reader.peek()
		when(peek) {
			JsonToken.NULL -> {
				reader.nextNull()
				return null
			}
			JsonToken.NUMBER -> {
				return reader.nextInt().toIntBool()
			}
			JsonToken.BOOLEAN -> {
				return reader.nextBoolean().toIntBool()
			}
			else -> return reader.nextString().toIntBool()
		}
	}
}