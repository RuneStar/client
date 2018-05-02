package org.runestar.client.game.api

import org.kxtra.swing.polygon.Polygon
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Projections
import java.awt.Polygon
import kotlin.math.abs
import kotlin.math.max

data class SceneTile(
        val x: Int,
        val y: Int,
        val plane: Int
) {

    val center get() = Position(x, Position.MID_SUB, y, Position.MID_SUB, 0, plane)

    val base get() = Position(x, 0, y, 0, 0, plane)

    val isLoaded get() = x in 0 until Scene.SIZE && y in 0 until Scene.SIZE && plane in 0 until Scene.PLANE_SIZE

    @JvmOverloads
    fun toGlobalTile(scene: Scene = LiveScene): GlobalTile {
        return GlobalTile(x + scene.base.x, y + scene.base.y, plane)
    }

    @JvmOverloads
    fun height(scene: Scene = LiveScene): Int {
        return scene.getHeight(this)
    }

    operator fun plus(sceneTile: SceneTile) : SceneTile {
        return SceneTile(x + sceneTile.x, y + sceneTile.y, plane + sceneTile.plane)
    }

    val corners: List<Position>
        get() = base.run {
            listOf(
                    this,
                    copy(subX = Position.MAX_SUB),
                    copy(subX = Position.MAX_SUB, subY = Position.MAX_SUB),
                    copy(subY = Position.MAX_SUB)
            )
        }

    @JvmOverloads
    fun outline(projection: Projection = Projections.viewport): Polygon {
        check(isLoaded) { this }
        return Polygon(corners.mapNotNull { it.toScreen(projection) })
    }

    /**
     * Chebyshev distance, ignoring planes
     */
    fun distanceTo(other: SceneTile): Int {
        return max(abs(x - other.x), abs(y - other.y))
    }
}