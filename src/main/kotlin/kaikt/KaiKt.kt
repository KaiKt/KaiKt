package kaikt

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kaikt.api.util.IntBool
import kaikt.api.util.IntBoolTypeAdapter

class KaiKt {
}

val gson: Gson = GsonBuilder().registerTypeAdapter(IntBool::class.java, IntBoolTypeAdapter()).create()