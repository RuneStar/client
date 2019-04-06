package org.runestar.client.game.api

import org.runestar.client.game.api.live.Projections
import java.awt.Point

data class Position(
        val localX: Int,
        val localY: Int,
        val height: Int,
        val plane: Int
) {
    constructor(x: Int, subX: Int, y: Int, subY: Int, height: Int, plane: Int) :
            this(LocalValue(x, subX).value, LocalValue(y, subY).value, height, plane)

    val x get() = LocalValue(localX).scene

    val y get() = LocalValue(localY).scene

    val subX get() = LocalValue(localX).sub

    val subY get() = LocalValue(localY).sub

    val sceneTile get() = SceneTile(x, y, plane)

    val isLoaded get() = isLoaded(localX, localY, plane)

    fun plusLocal(localX: Int = 0, localY: Int = 0, height: Int = 0): Position {
        return Position(localX + this.localX, localY + this.localY, height + this.height, plane)
    }

    operator fun plus(position: Position) =
            Position(position.localX + localX, position.localY + localY, position.height + height, position.plane + plane)

    fun toScreen(projection: Projection = Projections.viewport): Point? {
        return projection.toScreen(this)
    }

    companion object {

        fun isLoaded(localX: Int, localY: Int, plane: Int): Boolean {
            return SceneTile.isLoaded(LocalValue(localX).scene, LocalValue(localY).scene, plane)
        }

        fun centerOfTile(x: Int, y: Int, height: Int, plane: Int): Position {
            return Position(x, LocalValue.MID_SUB, y, LocalValue.MID_SUB, height, plane)
        }
    }
}