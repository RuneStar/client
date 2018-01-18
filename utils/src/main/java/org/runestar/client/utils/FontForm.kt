package org.runestar.client.utils

import org.kxtra.swing.font.Font
import java.awt.Font
import java.util.function.Supplier

data class FontForm(
        val name: String,
        val style: String,
        val size: Float
) : Supplier<Font> {

    constructor(font: Font) : this(font.name, styleToString(font.style), font.size2D)

    companion object {
        const val PLAIN = "plain"
        const val BOLD = "bold"
        const val ITALIC = "italic"
        const val BOLDITALIC = "bolditalic"

        @JvmStatic
        fun styleToString(style: Int): String {
            return when (style) {
                Font.PLAIN -> PLAIN
                Font.BOLD -> BOLD
                Font.ITALIC -> ITALIC
                Font.BOLD or Font.ITALIC -> BOLDITALIC
                else -> throw IllegalArgumentException("Invalid font style")
            }
        }
    }

    @Transient
    private val value = Font(name, style, size)

    override fun get() = value
}