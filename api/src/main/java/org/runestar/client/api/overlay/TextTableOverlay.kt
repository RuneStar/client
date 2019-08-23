package org.runestar.client.api.overlay

import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics2D
import kotlin.math.max

class TextTableOverlay(
        val map: Map<String, ColoredString>,
        val keyColor: Color,
        val font: Font,
        val lineSpacing: Int,
        val dividingWidth: Int
) : ModifiableOverlay() {

    init {
        require(lineSpacing >= 0 && dividingWidth >= 0)
    }

    override fun draw(g: Graphics2D, size: Dimension) {
        g.font = font
        val fm = g.fontMetrics
        var baseline = fm.ascent
        for ((k, v) in map) {
            g.color = keyColor
            g.drawString(k, 0, baseline)
            g.color = v.color
            g.drawString(v.string, size.width - fm.stringWidth(v.string), baseline)
            baseline += fm.height + lineSpacing
        }
    }

    override fun getSize0(g: Graphics2D, result: Dimension) {
        if (map.isEmpty()) return result.setSize(0, 0)
        val fm = g.getFontMetrics(font)
        result.height = fm.height * map.size + (map.size - 1) * lineSpacing
        var width = 0
        for ((k, v) in map) {
            width = max(width, fm.stringWidth(k) + fm.stringWidth(v.string))
        }
        result.width = width + dividingWidth
    }
}