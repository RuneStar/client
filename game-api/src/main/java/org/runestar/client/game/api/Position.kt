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
            this(toLocal(x, subX), toLocal(y, subY), height, plane)

    val x get() = toScene(localX)

    val y get() = toScene(localY)

    val subX get() = toSub(localX)

    val subY get() = toSub(localY)

    val sceneTile get() = SceneTile(x, y, plane)

    val isLoaded get() = isLoaded(localX, localY, plane)

    fun plusLocal(localX: Int = 0, localY: Int = 0, height: Int = 0): Position {
        return Position(localX + this.localX, localY + this.localY, height + this.height, plane)
    }

    operator fun plus(position: Position) =
            Position(position.localX + localX, position.localY + localY, position.height + height, position.plane + plane)

    fun toScreen(projection: Projection = Projections.viewport): Point? {
        check(isLoaded) { this }
        return projection.toScreen(this)
    }

    companion object {

        const val MAX_SUB = 127
        const val MID_SUB = 64
        const val MIN_SUB = 0

        val MAX_LOCAL = toLocal(Scene.SIZE - 1, MAX_SUB)

        fun toScene(local: Int): Int {
            return local shr 7
        }

        fun toSub(local: Int): Int {
            return local and MAX_SUB
        }

        fun toLocal(scene: Int, sub: Int): Int {
            return scene shl 7 or sub
        }

        fun isLoaded(localX: Int, localY: Int, plane: Int): Boolean {
            return SceneTile.isLoaded(toScene(localX), toScene(localY), plane)
        }
    }
}