package org.runestar.client.game.api

import java.awt.Color
import java.util.function.UnaryOperator

interface TextEffect : UnaryOperator<String> {

    val openTag: String

    val closeTag: String

    override fun apply(s: String): String = "$openTag$s$closeTag"

    data class Simple(
        val type: Type,
        val rgb: Color
    ) : TextEffect {

        override val openTag: String get() = "<${type.text}=${(rgb.rgb and 0xFFFFFF).toString(16)}>"

        override val closeTag: String get() = "</${type.text}>"

    }

    data class Composite(
            val effects: Iterable<TextEffect>
    ) : TextEffect {

        override val openTag: String = effects.joinToString("") { it.openTag }

        override val closeTag: String = effects.joinToString("") { it.closeTag }
    }

    enum class Type(val text: String) {
        UNDERLINE("u"),
        STRIKE("str"),
        COLOR("col"),
        SHADOW("shad")
    }
}