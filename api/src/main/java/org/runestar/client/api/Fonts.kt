package org.runestar.client.api

import org.kxtra.slf4j.getLogger
import java.awt.Font
import java.awt.GraphicsEnvironment

object Fonts {

    @JvmField val PLAIN_11 = createFont("RuneScape-Plain-11", 16)

    @JvmField val PLAIN_12 = createFont("RuneScape-Plain-12", 16)

    @JvmField val BOLD_12 = createFont("RuneScape-Bold-12", 16)

    private fun createFont(name: String, size: Int): Font {
        javaClass.getResourceAsStream("$name.ttf").use { input ->
            val font = Font.createFont(Font.TRUETYPE_FONT, input).deriveFont(size.toFloat())
            if (!GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)) {
                getLogger().warn("Failed to register font $font. A font with a similar name may already be installed.")
            }
            return font
        }
    }
}