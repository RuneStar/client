package org.runestar.client.api.overlay

import org.kxtra.swing.geom.component1
import org.kxtra.swing.geom.component2
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.absoluteValue
import kotlin.math.max

class OverlayList(
        val overlays: Collection<Overlay>,
        val direction: Int,
        val alignment: Int,
        val spacing: Int
) : Overlay {

    companion object {
        const val CENTER = 0
        const val UP = -1
        const val DOWN = 1
        const val LEFT = -2
        const val RIGHT = 2
    }

    init {
        require(spacing >= 0)
        val dv = direction.absoluteValue
        val av = alignment.absoluteValue
        require(dv in 1..2 && av in 0..2 && dv != av)
    }

    override fun draw(g: Graphics2D, size: Dimension) {
        when (overlays.size) {
            0 -> return
            1 -> return overlays.first().draw(g, size)
        }
        val (w, h) = size
        val tx = g.transform
        when (direction) {
            UP -> {
                g.translate(0, h)
                overlays.forEach {
                    it.getSize(g, size)
                    if (size.isZero) return@forEach
                    val (ow, oh) = size
                    var x = 0
                    when (alignment) {
                        CENTER -> x = (w - ow) / 2
                        RIGHT -> x = w - ow
                    }
                    g.translate(x, -oh)
                    it.draw(g, size)
                    g.translate(-x, -spacing)
                }
            }
            DOWN -> {
                overlays.forEach {
                    it.getSize(g, size)
                    if (size.isZero) return@forEach
                    val (ow, oh) = size
                    var x = 0
                    when (alignment) {
                        CENTER -> x = (w - ow) / 2
                        RIGHT -> x = w - ow
                    }
                    g.translate(x, 0)
                    it.draw(g, size)
                    g.translate(-x, oh + spacing)
                }
            }
            LEFT -> {
                g.translate(size.width, 0)
                overlays.forEach {
                    it.getSize(g, size)
                    if (size.isZero) return@forEach
                    val (ow, oh) = size
                    var y = 0
                    when (alignment) {
                        CENTER -> y = (h - oh) / 2
                        DOWN -> y = h - oh
                    }
                    g.translate(-ow, y)
                    it.draw(g, size)
                    g.translate(-spacing, -y)
                }
            }
            RIGHT -> {
                overlays.forEach {
                    it.getSize(g, size)
                    if (size.isZero) return@forEach
                    val (ow, oh) = size
                    var y = 0
                    when (alignment) {
                        CENTER -> y = (h - oh) / 2
                        DOWN -> y = h - oh
                    }
                    g.translate(0, y)
                    it.draw(g, size)
                    g.translate(ow + spacing, -y)
                }
            }
        }
        g.transform = tx
    }

    override fun getSize(g: Graphics2D, result: Dimension) {
        when (overlays.size) {
            0 -> return result.setSize(0, 0)
            1 -> return overlays.first().getSize(g, result)
        }
        var w = 0
        var h = 0
        when (direction) {
            UP, DOWN -> {
                overlays.forEach {
                    it.getSize(g, result)
                    w = max(w, result.width)
                    h += result.height + spacing
                }
                h -= spacing
            }
            LEFT, RIGHT -> {
                overlays.forEach {
                    it.getSize(g, result)
                    w += result.width + spacing
                    h = max(h, result.height)
                }
                w -= spacing
            }
        }
        result.setSize(w, h)
    }

    private val Dimension.isZero get() = width == 0 && height == 0
}