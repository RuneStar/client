package org.runestar.client.game.api.live

import org.runestar.client.game.api.Viewport
import org.runestar.client.game.raw.CLIENT

object LiveViewport : Viewport {

    override val x: Int get() = CLIENT.viewportOffsetX

    override val y: Int get() = CLIENT.viewportOffsetY

    override val width: Int get() = CLIENT.viewportWidth

    override val height: Int get() = CLIENT.viewportHeight

    override val zoom: Int get() = CLIENT.viewportZoom

    override fun toString(): String {
        return "LiveViewport(zoom=$zoom, shape=$shape)"
    }
}