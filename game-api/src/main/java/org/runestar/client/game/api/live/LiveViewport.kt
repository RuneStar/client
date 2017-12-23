package org.runestar.client.game.api.live

import org.runestar.client.game.api.Viewport
import org.runestar.client.game.raw.Client.accessor
import java.awt.Rectangle

object LiveViewport : Viewport {

    override val x: Int get() = accessor.viewportOffsetX

    override val y: Int get() = accessor.viewportOffsetY

    override val width: Int get() = accessor.viewportWidth

    override val height: Int get() = accessor.viewportHeight

    override val zoom: Int get() = accessor.viewportZoom

    override fun toString(): String {
        return "LiveViewport(zoom=$zoom, shape=$shape)"
    }
}