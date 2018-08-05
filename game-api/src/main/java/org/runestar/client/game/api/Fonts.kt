package org.runestar.client.game.api

import java.awt.Font
import java.awt.GraphicsEnvironment

object Fonts {

    val PLAIN_11 = getFont("RuneScape-Plain-11", 16)

    val PLAIN_12 = getFont("RuneScape-Plain-12", 16)

    val BOLD_12 = getFont("RuneScape-Bold-12", 16)

    val QUILL_8 = getFont("RuneScape-Quill-8", 32)

    val QUILL = getFont("RuneScape-Quill", 32)

    private fun getFont(name: String, size: Int): Font =
            Font.createFont(Font.TRUETYPE_FONT, javaClass.getResourceAsStream("$name.ttf"))
                    .deriveFont(size.toFloat())
                    .also { GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(it) }
}