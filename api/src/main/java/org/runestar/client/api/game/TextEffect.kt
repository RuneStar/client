package org.runestar.client.api.game

import java.awt.Color
import java.util.function.UnaryOperator

data class TextEffect(
        val type: Type,
        val rgb: Color
) : UnaryOperator<String> {

    @Transient val openTag = "<${type.text}=${(rgb.rgb and 0xFFFFFF).toString(16)}>"

    @Transient val closeTag = "</${type.text}>"

    override fun apply(s: String) = "$openTag$s$closeTag"

    enum class Type(val text: String) {
        UNDERLINE("u"),
        STRIKE("str"),
        COLOR("col"),
        SHADOW("shad")
    }
}