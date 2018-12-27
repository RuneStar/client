package org.runestar.client.game.api

import java.awt.Rectangle

interface Canvas {

    val shape: Rectangle

    object Fixed : Canvas {
        override val shape = Rectangle(0, 0, 765, 503)
    }
}