package org.runestar.client.api.overlay

import java.awt.Dimension
import java.awt.Graphics2D

class SizeCachingOverlay(
        val overlay: Overlay
) : Overlay {

    var modified = true

    private var width = -1

    private var height = -1

    override fun draw(g: Graphics2D, size: Dimension) = overlay.draw(g, size)

    override fun getSize(g: Graphics2D, result: Dimension) {
        if (!modified) return result.setSize(width, height)
        modified = false
        overlay.getSize(g, result)
        width = result.width
        height = result.height
    }
}

fun Overlay.cachingSize() = SizeCachingOverlay(this)