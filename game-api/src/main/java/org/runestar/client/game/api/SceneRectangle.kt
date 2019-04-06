package org.runestar.client.game.api

import org.runestar.client.game.api.live.Projections
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Path2D

data class SceneRectangle(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int,
        val plane: Int
) {

    init {
        require(width > 0 && height > 0 && SceneTile.isPlaneLoaded(plane))
    }

    val base: SceneTile get() = SceneTile(x, y, plane)

    fun outline(projection: Projection = Projections.viewport): Shape {
        val tempPoint = Point()
        val path = Path2D.Float(Path2D.WIND_NON_ZERO, width * 2 + height * 2 + 2)

        var x = LocalValue(x, 0).value
        var y = LocalValue(y, 0).value
        if (!projection.toScreen(x, y, 0, plane, tempPoint)) return path
        path.moveTo(tempPoint.x.toFloat(), tempPoint.y.toFloat())

        x += LocalValue.MAX_SUB
        lineTo(path, x, y, projection, tempPoint)
        repeat(width - 1) {
            x += LocalValue.MAX_SUB + 1
            lineTo(path, x, y, projection, tempPoint)
        }

        y += LocalValue.MAX_SUB
        lineTo(path, x, y, projection, tempPoint)
        repeat(height - 1) {
            y += LocalValue.MAX_SUB + 1
            lineTo(path, x, y, projection, tempPoint)
        }

        repeat(width - 1) {
            x -= LocalValue.MAX_SUB + 1
            lineTo(path, x, y, projection, tempPoint)
        }
        x -= LocalValue.MAX_SUB
        lineTo(path, x, y, projection, tempPoint)

        repeat(height - 1) {
            y -= LocalValue.MAX_SUB + 1
            lineTo(path, x, y, projection, tempPoint)
        }
        y -= LocalValue.MAX_SUB
        lineTo(path, x, y, projection, tempPoint)

        path.closePath()
        return path
    }

    private fun lineTo(path: Path2D.Float, localX: Int, localY: Int, projection: Projection, point: Point) {
        if (projection.toScreen(localX, localY, 0, plane, point)) {
            path.lineTo(point.x.toFloat(), point.y.toFloat())
        }
    }
}