package org.runestar.client.game.api

import org.kxtra.lang.list.subListTake
import org.kxtra.lang.list.subListTakeLast
import org.kxtra.swing.polygon.Polygon
import org.runestar.client.game.api.live.Projections
import org.runestar.client.game.raw.access.XModel
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon
import java.awt.Rectangle
import java.awt.geom.Area
import java.awt.geom.Line2D
import kotlin.math.max
import kotlin.math.min

class Model internal constructor(
        val accessor: XModel,
        val base: Position,
        val yaw: Angle
) {

    private val localXMin get() = base.localX + accessor.xMid - accessor.xMidOffset

    private val localXMax get() = base.localX + accessor.xMid + accessor.xMidOffset

    private val localYMin get() = base.localY + accessor.zMid - accessor.zMidOffset

    private val localYMax get() = base.localY + accessor.zMid + accessor.zMidOffset

    private val heightMin get() = base.height - (accessor.yMid + accessor.yMidOffset)

    private val heightMax get() = base.height - (accessor.yMid - accessor.yMidOffset)

    fun drawBoundingBox(
            g: Graphics2D,
            projection: Projection = Projections.viewport
    ) {
        boundingBoxEdges(projection).forEach {
            g.draw(it)
        }
    }

    fun boundingBoxEdges(projection: Projection = Projections.viewport): List<Line2D> {
        val pts = boundingBoxCornerPoints(projection)
        if (pts.isEmpty()) return emptyList()
        return listOf(
                Line2D.Double(pts[0], pts[1]),
                Line2D.Double(pts[1], pts[2]),
                Line2D.Double(pts[2], pts[3]),
                Line2D.Double(pts[3], pts[0]),

                Line2D.Double(pts[4], pts[5]),
                Line2D.Double(pts[5], pts[6]),
                Line2D.Double(pts[6], pts[7]),
                Line2D.Double(pts[7], pts[4]),

                Line2D.Double(pts[0], pts[4]),
                Line2D.Double(pts[1], pts[5]),
                Line2D.Double(pts[2], pts[6]),
                Line2D.Double(pts[3], pts[7])
        )
    }

    fun boundingBoxBottomCorners(): List<Position> {
        accessor.calculateBoundingBox(yaw.value) // todo
        val xMin = localXMin
        val xMax = localXMax
        val yMin = localYMin
        val yMax = localYMax
        val hMin = heightMin
        return listOf(
                Position(xMin, yMin, hMin, base.plane),
                Position(xMin, yMax, hMin, base.plane),
                Position(xMax, yMax, hMin, base.plane),
                Position(xMax, yMin, hMin, base.plane)
        )
    }

    fun boundingBoxBottomOutline(projection: Projection = Projections.viewport): Polygon {
        return Polygon(boundingBoxBottomCorners().mapNotNull { projection.toScreen(it, base) })
    }

    fun boundingBoxCorners(): List<Position> {
        accessor.calculateBoundingBox(yaw.value) // todo
        val xMin = localXMin
        val xMax = localXMax
        val yMin = localYMin
        val yMax = localYMax
        val hMin = heightMin
        val hMax = heightMax
        return listOf(
                Position(xMin, yMin, hMin, base.plane),
                Position(xMin, yMax, hMin, base.plane),
                Position(xMax, yMax, hMin, base.plane),
                Position(xMax, yMin, hMin, base.plane),

                Position(xMin, yMin, hMax, base.plane),
                Position(xMin, yMax, hMax, base.plane),
                Position(xMax, yMax, hMax, base.plane),
                Position(xMax, yMin, hMax, base.plane)
        )
    }

    fun boundingBoxCornerPoints(projection: Projection = Projections.viewport): List<Point> {
        return boundingBoxCorners().map { projection.toScreen(it, base) ?: return emptyList() }
    }

    fun boundingBoxFaces(projection: Projection = Projections.viewport): List<Polygon> {
        val pts = boundingBoxCornerPoints(projection)
        if (pts.isEmpty()) return emptyList()
        val bottom = Polygon(pts.subListTake(4))
        val top = Polygon(pts.subListTakeLast(4))
        val front = Polygon(listOf(pts[0], pts[3], pts[7], pts[4]))
        val back = Polygon(listOf(pts[1], pts[2], pts[6], pts[5]))
        val left = Polygon(listOf(pts[0], pts[1], pts[5], pts[4]))
        val right = Polygon(listOf(pts[2], pts[3], pts[7], pts[6]))
        return listOf(bottom, top, front, back, left, right)
    }

    fun boundingBoxOutline(projection: Projection = Projections.viewport): Area {
        return Area().apply {
            boundingBoxFaces(projection).forEach { add(Area(it)) }
        }
    }

    fun objectClickBoxOutline(projection: Projection = Projections.viewport): Area {
        return boundingBoxOutline(projection).apply { intersect(geometryOutline(projection)) }
    }

    fun geometryRectangles(projection: Projection = Projections.viewport): List<Rectangle> {
        val sin = yaw.sinInternal
        val cos = yaw.cosInternal
        val count = accessor.indicesCount
        val list = ArrayList<Rectangle>(count)
        for (i in 0 until count) {
            val a = vertexToScreen(accessor.indices1[i], projection, sin, cos) ?: continue
            val b = vertexToScreen(accessor.indices2[i], projection, sin, cos) ?: continue
            val c = vertexToScreen(accessor.indices3[i], projection, sin, cos) ?: continue

            val xMin = min(a.x, min(b.x, c.x)) - 4 // pad all sides by 4
            val yMin = min(a.y, min(b.y, c.y)) - 4
            val xMax = max(a.x, max(b.x, c.x)) + 4
            val yMax = max(a.y, max(b.y, c.y)) + 4

            val w = xMax - xMin
            val h = yMax - yMin

            list.add(Rectangle(xMin, yMin, w, h))
        }
        return list
    }

    fun geometryOutline(projection: Projection = Projections.viewport): Area {
        return Area().apply {
            for (rect in geometryRectangles(projection)) {
                if (!contains(rect)) {
                    add(Area(rect))
                }
            }
        }
    }

    fun drawTriangles(
            g: Graphics2D,
            projection: Projection = Projections.viewport
    ) {
        val sin = yaw.sinInternal
        val cos = yaw.cosInternal
        for (i in 0 until accessor.indicesCount) {
            val a = vertexToScreen(accessor.indices1[i], projection, sin, cos) ?: continue
            val b = vertexToScreen(accessor.indices2[i], projection, sin, cos) ?: continue
            val c = vertexToScreen(accessor.indices3[i], projection, sin, cos) ?: continue

            g.drawLine(a.x, a.y, b.x, b.y)
            g.drawLine(b.x, b.y, c.x, c.y)
            g.drawLine(c.x, c.y, a.x, a.y)
        }
    }

    fun fillTriangles(
            g: Graphics2D,
            projection: Projection = Projections.viewport
    ) {
        val sin = yaw.sinInternal
        val cos = yaw.cosInternal
        for (i in 0 until accessor.indicesCount) {
            val a = vertexToScreen(accessor.indices1[i], projection, sin, cos) ?: continue
            val b = vertexToScreen(accessor.indices2[i], projection, sin, cos) ?: continue
            val c = vertexToScreen(accessor.indices3[i], projection, sin, cos) ?: continue

            g.fillPolygon(intArrayOf(a.x, b.x, c.x), intArrayOf(a.y, b.y, c.y), 3)
        }
    }

    private fun vertexToScreen(i: Int, projection: Projection, sin: Int, cos: Int): Point? {
        val vx = accessor.verticesX[i]
        val vy = accessor.verticesY[i]
        val vz = accessor.verticesZ[i]

        val x = (vx * cos + vz * sin) shr 16
        val y = (vz * cos - vx * sin) shr 16
        val h = -1 * vy

        val pos = base.plusLocal(x, y, h)
        if (!pos.isLoaded) return null
        return projection.toScreen(pos, base)
    }
}