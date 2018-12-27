package org.runestar.client.game.api

import java.awt.Rectangle

interface Viewport {

    val zoom: Int

    val x: Int

    val y: Int

    val width: Int

    val height: Int

    val shape get() = Rectangle(x, y, width, height)

    data class Fixed(override val zoom: Int) : Viewport {

        override val x = 4
        override val y = 4
        override val width = 512
        override val height = 334

        companion object {
            const val ZOOM_DEFAULT = 512
            const val ZOOM_MIN = 390
            const val ZOOM_MAX = 1400

            @JvmField val DEFAULT = Fixed(ZOOM_DEFAULT)
        }
    }
}