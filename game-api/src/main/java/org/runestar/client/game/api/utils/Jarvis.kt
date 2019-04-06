package org.runestar.client.game.api.utils

import java.awt.Point
import java.awt.Shape
import java.awt.geom.Path2D

object Jarvis {

    fun convexHull(points: List<Point>): Shape {
        val ch = Path2D.Float()
        if (points.isEmpty()) {
            return ch
        }
        val leftMost = checkNotNull(points.minWith(leftMostComparator))
        ch.moveTo(leftMost.x.toFloat(), leftMost.y.toFloat())
        var curr = leftMost
        do {
            var next = points[0]
            for (i in 1..points.lastIndex) {
                val p = points[i]
                val orientation = orientation(curr, p, next)
                if (orientation > 0 || (orientation == 0 && curr.distance(p) > curr.distance(next))) {
                    next = p
                }
            }
            curr = next
            ch.lineTo(curr.x.toFloat(), curr.y.toFloat())
        } while (curr != leftMost)
        ch.closePath()
        return ch
    }

    private val leftMostComparator: Comparator<Point> = Comparator.comparingInt<Point> { it.x }
            .thenComparingInt { it.y }

    private fun orientation(p: Point, q: Point, r: Point): Int {
        return (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
    }
}