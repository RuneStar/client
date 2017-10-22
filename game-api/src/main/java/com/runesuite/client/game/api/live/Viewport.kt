package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.Client.accessor
import java.awt.Rectangle

interface Viewport {

    val zoom: Int

    val x: Int

    val y: Int

    val width: Int

    val height: Int

    val shape get() = Rectangle(x, y, width, height)

    object Live : Viewport {

        override val x: Int get() = accessor.viewportOffsetX

        override val y: Int get() = accessor.viewportOffsetY

        override val width: Int get() = accessor.viewportWidth

        override val height: Int get() = accessor.viewportHeight

        override val zoom: Int get() = accessor.viewportZoom

        override fun toString(): String {
            return "Viewport.Live(zoom=$zoom, shape=$shape)"
        }
    }

    data class Fixed(override val zoom: Int) : Viewport {

        override val x = 4
        override val y = 4
        override val width = 512
        override val height = 334

        companion object {
            const val ZOOM_DEFAULT = 512
            const val ZOOM_MIN = 390
            const val ZOOM_MAX = 1400

            @JvmField
            val DEFAULT = Fixed(ZOOM_DEFAULT)
        }
    }

    fun copyOf(): Copy = Copy(zoom, x, y, width, height)

    data class Copy(
            override val zoom: Int,
            override val x: Int,
            override val y: Int,
            override val width: Int,
            override val height: Int
    ) : Viewport
}