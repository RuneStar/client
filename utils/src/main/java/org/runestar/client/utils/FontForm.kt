package org.runestar.client.utils

import org.kxtra.swing.font.Font
import java.awt.Font
import java.util.function.Supplier

data class FontForm(
        val name: String,
        val style: String,
        val size: Float
) : Supplier<Font> {

    companion object {
        const val PLAIN = "plain"
        const val BOLD = "bold"
        const val ITALIC = "italic"
        const val BOLDITALIC = "bolditalic"
    }

    @Transient
    private val value = Font(name, style, size)

    override fun get() = value
}