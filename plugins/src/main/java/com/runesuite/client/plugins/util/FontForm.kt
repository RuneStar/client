package com.runesuite.client.plugins.util

import com.hunterwb.kxtra.swing.font.Font
import java.awt.Font
import java.util.function.Supplier

class FontForm(
        val name: String = Font.DIALOG,
        val style: String = PLAIN,
        val size: Float = 12f
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