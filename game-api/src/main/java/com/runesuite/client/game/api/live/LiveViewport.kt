package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Viewport
import com.runesuite.client.game.raw.Client.accessor
import java.awt.Rectangle

object LiveViewport : Viewport {

    override val x: Int get() = accessor.viewport_xOffset

    override val y: Int get() = accessor.viewport_yOffset

    override val width: Int get() = accessor.viewport_width

    override val height: Int get() = accessor.viewport_height

    override val zoom: Int get() = accessor.viewport_zoom

    override fun toString(): String {
        return "LiveViewport(zoom=$zoom, shape=$shape)"
    }
}