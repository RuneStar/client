package org.runestar.client.game.api

import org.runestar.client.game.api.live.Projections
import org.runestar.client.game.raw.access.XModel
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.Shape
import java.awt.geom.Area
import java.awt.geom.Path2D
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

    private fun <T> MutableList<T>.addNotNull(t: T?) {
        t?.let { add(it) }
    }

    private fun boundingBoxCornerPoints(projection: Projection = Projections.viewport): List<Point> {
        accessor.calculateBoundingBox(yaw.value) // todo
        val list = ArrayList<Point>(8)
        val xMin = localXMin
        val xMax = localXMax
        val yMin = localYMin
        val yMax = localYMax
        val hMin = heightMin
        val hMax = heightMax
        list.addNotNull(projection.toScreen(xMin, yMin, hMin, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMin, yMax, hMin, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMax, yMax, hMin, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMax, yMin, hMin, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMin, yMin, hMax, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMin, yMax, hMax, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMax, yMax, hMax, base.plane, base.localX, base.localY))
        list.addNotNull(projection.toScreen(xMax, yMin, hMax, base.plane, base.localX, base.localY))
        return list
    }

    fun boundingBoxOutline(projection: Projection = Projections.viewport): Shape {
        return Jarvis.convexHull(boundingBoxCornerPoints(projection))
    }

    fun objectClickBoxOutline(projection: Projection = Projections.viewport): Area {
        return Area(boundingBoxOutline(projection)).apply { intersect(geometryArea(projection)) }
    }

    fun geometryArea(projection: Projection = Projections.viewport): Area {
        val area = Area()
        trianglesForEach(projection) { x0, y0, x1, y1, x2, y2 ->

            val xMin = min(x0, min(x1, x2)) - 4 // pad all sides by 4
            val yMin = min(y0, min(y1, y2)) - 4
            val xMax = max(x0, max(x1, x2)) + 4
            val yMax = max(y0, max(y1, y2)) + 4

            val w = xMax - xMin
            val h = yMax - yMin

            if (!area.contains(xMin.toDouble(), yMin.toDouble(), w.toDouble(), h.toDouble())) {
                tempRectangle.setBounds(xMin, yMin, w, h)
                area.add(Area(tempRectangle))
            }
        }
        return area
    }

    fun drawTriangles(
            g: Graphics2D,
            projection: Projection = Projections.viewport
    ) {
        tempLargePath.reset()
        trianglesForEach(projection) { x0, y0, x1, y1, x2, y2 ->
            tempLargePath.appendTriangle(
                    x0.toFloat(), y0.toFloat(),
                    x1.toFloat(), y1.toFloat(),
                    x2.toFloat(), y2.toFloat()
            )
        }
        g.draw(tempLargePath)
    }

    fun fillTriangles(
            g: Graphics2D,
            projection: Projection = Projections.viewport
    ) {
        trianglesForEach(projection) { x0, y0, x1, y1, x2, y2 ->
            tempTrianglePath.reset()
            tempTrianglePath.appendTriangle(
                    x0.toFloat(), y0.toFloat(),
                    x1.toFloat(), y1.toFloat(),
                    x2.toFloat(), y2.toFloat()
            )
            g.fill(tempTrianglePath)
        }
    }

    private fun Path2D.Float.appendTriangle(
            x0: Float, y0: Float,
            x1: Float, y1: Float,
            x2: Float, y2: Float
    ) {
        moveTo(x0, y0)
        lineTo(x1, y1)
        lineTo(x2, y2)
        lineTo(x0, y0)
        closePath()
    }

    private inline fun trianglesForEach(
            projection: Projection,
            consumer: (Int, Int, Int, Int, Int, Int) -> Unit
    ) {
        for (i in 0 until accessor.indicesCount) {
            if (!vertexToScreen(accessor.indices1[i], projection, tempPoint)) continue
            val x0 = tempPoint.x
            val y0 = tempPoint.y
            if (!vertexToScreen(accessor.indices2[i], projection, tempPoint)) continue
            val x1 = tempPoint.x
            val y1 = tempPoint.y
            if (!vertexToScreen(accessor.indices3[i], projection, tempPoint)) continue
            val x2 = tempPoint.x
            val y2 = tempPoint.y
            consumer(x0, y0, x1, y1, x2, y2)
        }
    }

    private fun vertexToScreen(i: Int, projection: Projection, result: Point): Boolean {
        val vx = accessor.verticesX[i]
        val vy = accessor.verticesY[i]
        val vz = accessor.verticesZ[i]

        val x = (vx * yaw.cosInternal + vz * yaw.sinInternal) shr 16
        val y = (vz * yaw.cosInternal - vx * yaw.sinInternal) shr 16
        val h = -1 * vy

        val localX = base.localX + x
        val localY = base.localY + y
        if (!Position.isLoaded(localX, localY, base.plane)) return false
        return projection.toScreen(localX, localY, h, base.plane, base.localX, base.localY, result)
    }

    private companion object {
        val tempPoint = Point()
        val tempRectangle = Rectangle()
        val tempTrianglePath = Path2D.Float(Path2D.WIND_NON_ZERO, 6)
        val tempLargePath = Path2D.Float(Path2D.WIND_NON_ZERO, 5000)
    }
}