package org.runestar.client.api.overlay

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D

class OverlayBorder(
        val overlay: Overlay,
        val color: Color
) : Overlay {

    companion object {
        val DEFAULT = Color(14, 13, 15)
    }

    override fun draw(g: Graphics2D, size: Dimension) {
        g.color = color
        g.drawRect(0, 0, size.width - 1, size.height - 1)
        g.translate(1, 1)
        size.setSize(size.width - 2, size.height - 2)
        overlay.draw(g, size)
        g.translate(-1, -1)
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        overlay.getSize(g, result)
        result.setSize(result.width + 2, result.height + 2)
    }
}

fun Overlay.withBorder(color: Color) = OverlayBorder(this, color)

fun Overlay.withBorder() = withBorder(OverlayBorder.DEFAULT)