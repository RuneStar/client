package org.runestar.client.api.overlay

import java.awt.Dimension
import java.awt.Graphics2D

class HideableOverlay(
        val overlay: Overlay
) : Overlay {

    var show = true

    override fun draw(g: Graphics2D, size: Dimension) {
        if (show) overlay.draw(g, size)
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        if (show) overlay.getSize(g, result) else result.setSize(0, 0)
    }
}

fun Overlay.hideable() = HideableOverlay(this)