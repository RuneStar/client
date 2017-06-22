package com.runesuite.client.game

/**
 * Exact point in 3D game space
 */
data class Position(
        val x: Int,
        val subX: Int,
        val y: Int,
        val subY: Int,
        val height: Int = 0,
        val plane: Int = 0
) {
    internal constructor(localX: Int, localY: Int, height: Int = 0, plane: Int = 0) :
            this(localX shr 7, localX and MAX_SUB, localY shr 7, localY and MAX_SUB, height, plane)

    internal val localX get() = (x shl 7) or subX

    internal val localY get() = (y shl 7) or subY

    val sceneTile get() = SceneTile(x, y, plane)

    val isLoaded get() = sceneTile.isLoaded

    internal fun plusLocal(localX: Int = 0, localY: Int = 0, height: Int = 0): Position {
        return Position(localX + this.localX, localY + this.localY, height + this.height, plane)
    }

    operator fun plus(position: Position) =
            Position(position.localX + localX, position.localY + localY, position.height + height, position.plane + plane)

    companion object {
        const val MAX_SUB = 127
        const val MID_SUB = 64
        const val MIN_SUB = 0
    }
}


