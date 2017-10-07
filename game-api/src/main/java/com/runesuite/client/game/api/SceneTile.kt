package com.runesuite.client.game.api

import com.hunterwb.kxtra.swing.polygon.Polygon
import com.runesuite.client.game.api.live.Projection
import com.runesuite.client.game.api.live.Scene
import java.awt.Polygon

/**
 * Tile relative to a Scene
 *
 * @see[Scene]
 */
data class SceneTile(val x: Int, val y: Int, val plane: Int) {

    val center get() = com.runesuite.client.game.api.Position(x, com.runesuite.client.game.api.Position.Companion.MID_SUB, y, com.runesuite.client.game.api.Position.Companion.MID_SUB, 0, plane)

    val base get() = com.runesuite.client.game.api.Position(x, 0, y, 0, 0, plane)

    val isLoaded get() = x in 0 until Scene.SIZE && y in 0 until Scene.SIZE && plane in 0 until Scene.PLANE_SIZE

    fun toGlobalTile(scene: Scene = Scene.Live): com.runesuite.client.game.api.GlobalTile {
        return com.runesuite.client.game.api.GlobalTile(x + scene.base.x, y + scene.base.y, plane)
    }

    fun height(scene: Scene = Scene.Live): Int? {
        return scene.getHeight(this)
    }

    operator fun plus(sceneTile: com.runesuite.client.game.api.SceneTile) : com.runesuite.client.game.api.SceneTile {
        return com.runesuite.client.game.api.SceneTile(x + sceneTile.x, y + sceneTile.y, plane + sceneTile.plane)
    }

    val corners: List<com.runesuite.client.game.api.Position>
        get() = base.run { listOf(
                this,
                copy(subX = com.runesuite.client.game.api.Position.Companion.MAX_SUB),
                copy(subX = com.runesuite.client.game.api.Position.Companion.MAX_SUB, subY = com.runesuite.client.game.api.Position.Companion.MAX_SUB),
                copy(subY = com.runesuite.client.game.api.Position.Companion.MAX_SUB)) }

    fun outline(projection: Projection = Projection.Viewport.Live): Polygon {
        check(isLoaded) { this }
        return Polygon(corners.map { it.toScreen(projection) })
    }
}
