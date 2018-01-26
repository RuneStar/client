package org.runestar.client.utils

import org.kxtra.swing.font.Font
import java.awt.Font
import java.util.function.Supplier

data class FontForm(
        val name: String,
        val style: String,
        val size: Float
) : Supplier<Font> {

    constructor(font: Font) : this(font.name, Style.of(font.style).name, font.size2D)

    enum class Style(val asInt: Int) {
        PLAIN(Font.PLAIN),
        BOLD(Font.BOLD),
        ITALIC(Font.ITALIC),
        BOLDITALIC(Font.BOLD or Font.ITALIC);

        companion object {
            @JvmField val VALUES = values().asList()

            @JvmStatic
            fun of(style: Int): Style = VALUES.first { it.asInt == style }

            @JvmStatic
            fun of(style: String): Style = VALUES.first { it.name == style }
        }
    }

    @Transient
    private val value = Font(name, Style.of(style).asInt, size)

    override fun get() = value
}