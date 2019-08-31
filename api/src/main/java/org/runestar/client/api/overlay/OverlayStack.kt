package org.runestar.client.api.overlay

import org.kxtra.swing.geom.component1
import org.kxtra.swing.geom.component2
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

class OverlayStack(
        val front: Overlay,
        val back: Overlay
) : Overlay {

    override fun draw(g: Graphics2D, size: Dimension) {
        val (w, h) = size
        back.getSize(g, size)
        val xb = (w - size.width) / 2
        val yb = (h - size.height) / 2
        g.translate(xb, yb)
        back.draw(g, size)
        front.getSize(g, size)
        val xf = (w - size.width) / 2
        val yf = (h - size.height) / 2
        g.translate(xf - xb, yf - yb)
        front.draw(g, size)
        g.translate(-xf, -yf)
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        front.getSize(g, result)
        val (wf, hf) = result
        back.getSize(g, result)
        val (wb, hb) = result
        result.setSize(max(wf, wb), max(hf, hb))
    }
}