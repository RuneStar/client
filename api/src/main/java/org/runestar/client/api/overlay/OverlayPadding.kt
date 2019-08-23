package org.runestar.client.api.overlay

import org.runestar.client.api.forms.InsetsForm
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Insets

class OverlayPadding(
        val overlay: Overlay,
        val top: Int,
        val left: Int,
        val bottom: Int,
        val right: Int
) : Overlay {

    override fun draw(g: Graphics2D, size: Dimension) {
        if (size.width == 0 && size.height == 0) return
        g.translate(left, top)
        size.setSize(size.width - left - right, size.height - top - bottom)
        overlay.draw(g, size)
        g.translate(-left, -top)
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        overlay.getSize(g, result)
        if (result.width == 0 && result.height == 0) return
        result.setSize(result.width + left + right, result.height + top + bottom)
    }
}

fun Overlay.withPadding(top: Int, left: Int, bottom: Int, right: Int) = OverlayPadding(this, top, left, bottom, right)

fun Overlay.withPadding(insets: Insets) = withPadding(insets.top, insets.left, insets.bottom, insets.right)

fun Overlay.withPadding(insets: InsetsForm) = withPadding(insets.top, insets.left, insets.bottom, insets.right)

fun Overlay.withPadding(size: Int) = withPadding(size, size, size, size)