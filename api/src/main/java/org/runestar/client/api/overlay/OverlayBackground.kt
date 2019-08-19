package org.runestar.client.api.overlay

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Paint

class OverlayBackground(
        val overlay: Overlay,
        val paint: Paint
) : Overlay {

    companion object {
        val DEFAULT = Color(70, 61, 50, 156)
    }

    override fun draw(g: Graphics2D, size: Dimension) {
        g.paint = paint
        g.fillRect(0, 0, size.width, size.height)
        overlay.draw(g, size)
    }

    override fun getSize(g: Graphics2D, result: Dimension) = overlay.getSize(g, result)
}

fun Overlay.withBackground(paint: Paint) = OverlayBackground(this, paint)

fun Overlay.withBackground() = withBackground(OverlayBackground.DEFAULT)