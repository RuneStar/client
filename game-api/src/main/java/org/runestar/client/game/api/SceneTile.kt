package org.runestar.client.game.api

import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Projections
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Path2D
import kotlin.math.abs
import kotlin.math.max

data class SceneTile(
        val x: Int,
        val y: Int,
        val plane: Int
) {

    companion object {

        fun isXyLoaded(n: Int): Boolean {
            return n in 0 until Scene.SIZE
        }

        fun isPlaneLoaded(plane: Int): Boolean {
            return plane in 0 until Scene.PLANE_SIZE
        }

        fun isLoaded(x: Int, y: Int, plane: Int): Boolean {
            return isXyLoaded(x) && isXyLoaded(y) && isPlaneLoaded(plane)
        }
    }

    val center get() = Position.centerOfTile(x, y, 0, plane)

    val base get() = Position(x, 0, y, 0, 0, plane)

    val isLoaded get() = isLoaded(x, y, plane)

    fun toGlobalTile(scene: Scene = LiveScene): GlobalTile {
        return GlobalTile(x + scene.base.x, y + scene.base.y, plane)
    }

    fun height(scene: Scene = LiveScene): Int {
        return scene.getHeight(this)
    }

    operator fun plus(sceneTile: SceneTile) : SceneTile {
        return SceneTile(x + sceneTile.x, y + sceneTile.y, plane + sceneTile.plane)
    }

    val corners: List<Position>
        get() = listOf(
                Position(x, 0, y, 0, 0, plane),
                Position(x, Position.MAX_SUB, y, 0, 0, plane),
                Position(x, Position.MAX_SUB, y, Position.MAX_SUB, 0, plane),
                Position(x, 0, y, Position.MAX_SUB, 0, plane)
        )

    fun outline(projection: Projection = Projections.viewport): Shape {
        val tempPoint = Point()

        val path = Path2D.Float(Path2D.WIND_NON_ZERO, 6)

        val xMin = Position.toLocal(x, 0)
        val yMin = Position.toLocal(y, 0)
        val xMax = Position.toLocal(x, Position.MAX_SUB)
        val yMax = Position.toLocal(y, Position.MAX_SUB)

        if (!projection.toScreen(xMin, yMin, 0, plane, tempPoint)) return path
        val x0 = tempPoint.x.toFloat()
        val y0 = tempPoint.y.toFloat()
        if (!projection.toScreen(xMax, yMin, 0, plane, tempPoint)) return path
        val x1 = tempPoint.x.toFloat()
        val y1 = tempPoint.y.toFloat()
        if (!projection.toScreen(xMax, yMax, 0, plane, tempPoint)) return path
        val x2 = tempPoint.x.toFloat()
        val y2 = tempPoint.y.toFloat()
        if (!projection.toScreen(xMin, yMax, 0, plane, tempPoint)) return path
        val x3 = tempPoint.x.toFloat()
        val y3 = tempPoint.y.toFloat()

        path.appendPolygon4(x0, y0, x1, y1, x2, y2, x3, y3)

        return path
    }

    private fun Path2D.Float.appendPolygon4(
            x0: Float, y0: Float,
            x1: Float, y1: Float,
            x2: Float, y2: Float,
            x3: Float, y3: Float
    ) {
        moveTo(x0, y0)
        lineTo(x1, y1)
        lineTo(x2, y2)
        lineTo(x3, y3)
        lineTo(x0, y0)
        closePath()
    }

    /**
     * Chebyshev distance, ignoring planes
     */
    fun distanceTo(other: SceneTile): Int {
        return max(abs(x - other.x), abs(y - other.y))
    }

    operator fun plus(direction: OctantDirection): SceneTile {
        return SceneTile(x + direction.x, y + direction.y, plane)
    }
}