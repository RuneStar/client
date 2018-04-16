package org.runestar.client.game.api

import org.kxtra.swing.polygon.Polygon
import org.runestar.client.game.api.live.Projections
import org.runestar.client.game.raw.access.XModel
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Polygon

class Model(override val accessor: XModel) : Wrapper(accessor) {

    private val localXMin get() = accessor.xMid - accessor.xMidOffset

    private val localXMax get() = accessor.xMid + accessor.xMidOffset

    private val localYMin get() = accessor.zMid - accessor.zMidOffset

    private val localYMax get() = accessor.zMid + accessor.zMidOffset

    private val heightMin get() = -1 * (accessor.yMid + accessor.yMidOffset)

    private val heightMax get() = -1 * (accessor.yMid - accessor.yMidOffset)

    fun drawBoundingBox(
            g: Graphics2D,
            base: Position,
            yaw: Angle = Angle.ZERO,
            projection: Projection = Projections.viewport
    ) {
        accessor.calculateBoundingBox(yaw.value) // todo
        val xMin = base.localX + localXMin
        val xMax = base.localX + localXMax
        val yMin = base.localY + localYMin
        val yMax = base.localY + localYMax
        val hMin = base.height - heightMin
        val hMax = base.height - heightMax
        for (i in 0 until 8) {
            val x0 = if (i and 1 == 0) xMin else xMax
            val y0 = if (i and 2 == 0) yMin else yMax
            val h0 = if (i and 4 == 0) hMin else hMax
            val p0 = Position(x0, y0, h0, base.plane)
            val pt0 = projection.toScreen(p0, base) ?: continue
            if (i and 1 == 0) {
                val pt1 = projection.toScreen(Position(xMax, y0, h0, base.plane), base)
                if (pt1 != null) {
                    g.drawLine(pt0.x, pt0.y, pt1.x, pt1.y)
                }
            }
            if (i and 2 == 0) {
                val pt1 = projection.toScreen(Position(x0, yMax, h0, base.plane), base)
                if (pt1 != null) {
                    g.drawLine(pt0.x, pt0.y, pt1.x, pt1.y)
                }
            }
            if (i and 4 == 0) {
                val pt1 = projection.toScreen(Position(x0, y0, hMax, base.plane), base)
                if (pt1 != null) {
                    g.drawLine(pt0.x, pt0.y, pt1.x, pt1.y)
                }
            }
        }
    }

    fun boundingBoxBottomCorners(
            base: Position,
            yaw: Angle = Angle.ZERO
    ): List<Position> {
        accessor.calculateBoundingBox(yaw.value) // todo
        return listOf(
                base.plusLocal(localXMin, localYMin, heightMin),
                base.plusLocal(localXMax, localYMin, heightMin),
                base.plusLocal(localXMax, localYMax, heightMin),
                base.plusLocal(localXMin, localYMax, heightMin)
        )
    }

    fun boundingBoxBottomOutline(
            base: Position,
            yaw: Angle = Angle.ZERO,
            projection: Projection = Projections.viewport
    ): Polygon {
        return Polygon(boundingBoxBottomCorners(base, yaw).mapNotNull { projection.toScreen(it, base) })
    }
}