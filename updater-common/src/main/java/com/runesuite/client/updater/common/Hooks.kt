package com.runesuite.client.updater.common

data class ClassHook(
        val `class`: String,
        val name: String,
        val `super`: String,
        val access : Int,
        val interfaces: List<String>,
        val fields: List<FieldHook>,
        val methods: List<MethodHook>
)

data class FieldHook(
        val field: String,
        val owner: String,
        val name: String,
        val access: Int,
        val descriptor: String,
        val decoder: Long?
)

val FieldHook.decoderNarrowed: Number? get() = when(decoder) {
    null -> null
    in Int.MIN_VALUE..Int.MAX_VALUE -> decoder.toInt()
    else -> decoder
}

data class MethodHook(
        val method: String,
        val owner: String,
        val name: String,
        val access: Int,
        val parameters: List<String>?,
        val descriptor: String,
        val finalArgument: Int?
)

val MethodHook.finalArgumentNarrowed: Number? get() = when(finalArgument) {
    null -> null
    in Byte.MIN_VALUE..Byte.MAX_VALUE -> finalArgument.toByte()
    in Short.MIN_VALUE..Short.MAX_VALUE -> finalArgument.toShort()
    else -> finalArgument
}