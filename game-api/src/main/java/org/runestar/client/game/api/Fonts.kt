package org.runestar.client.game.api

import java.awt.Font
import java.awt.GraphicsEnvironment

object Fonts {

    val SMALL = getFont("RuneScape-Small", 16)

    val CHAT = getFont("RuneScape-Chat", 16)

    val CHAT_BOLD = getFont("RuneScape-Chat-Bold", 16)

    val NPC_CHAT = getFont("RuneScape-NPC-Chat", 32)

    val LARGE = getFont("RuneScape-Large", 32)

    private fun getFont(name: String, size: Int): Font =
            Font.createFont(Font.TRUETYPE_FONT, javaClass.getResourceAsStream("$name.ttf"))
                    .deriveFont(size.toFloat())
                    .also { GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(it) }
}