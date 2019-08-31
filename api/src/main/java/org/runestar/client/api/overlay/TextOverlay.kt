package org.runestar.client.api.overlay

import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics2D

class TextOverlay(
        var string: String,
        var font: Font,
        var color: Color
) : Overlay {

    override fun draw(g: Graphics2D, size: Dimension) {
        g.color = color
        g.font = font
        g.drawString(string, 0, g.fontMetrics.ascent)
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        val fm = g.getFontMetrics(font)
        result.setSize(fm.stringWidth(string), fm.height)
    }
}