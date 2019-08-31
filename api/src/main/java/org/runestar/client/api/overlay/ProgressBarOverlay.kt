package org.runestar.client.api.overlay

import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Paint
import kotlin.math.ceil

class ProgressBarOverlay(
        val width: Int,
        val height: Int,
        val front: Paint,
        val back: Paint
) : Overlay {

    private var progressInt = 0

    var progress: Double
        get() = progressInt.toDouble() / width
        set(value) { progressInt = ceil(width * value).toInt() }

    override fun draw(g: Graphics2D, size: Dimension) {
        g.paint = front
        g.fillRect(0, 0, progressInt, height)
        g.paint = back
        g.fillRect(progressInt, 0, width - progressInt, height)
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        result.setSize(width, height)
    }
}