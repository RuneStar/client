package org.runestar.client.utils

import org.kxtra.swing.font.Font
import java.awt.Font
import java.util.function.Supplier

data class FontForm(
        val name: String,
        val style: Style,
        val size: Float
) : Supplier<Font> {

    constructor(font: Font) : this(font.name, Style.of(font.style), font.size2D)

    enum class Style(val asInt: Int) {
        PLAIN(Font.PLAIN),
        BOLD(Font.BOLD),
        ITALIC(Font.ITALIC),
        BOLDITALIC(Font.BOLD or Font.ITALIC);

        companion object {
            @JvmField val VALUES = values().asList()

            @JvmStatic
            fun of(style: Int): Style = VALUES.first { it.asInt == style }
        }
    }

    @Transient
    private val value = Font(name, style.asInt, size)

    override fun get() = value
}