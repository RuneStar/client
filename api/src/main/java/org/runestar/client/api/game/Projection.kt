package org.runestar.client.api.game

import java.awt.Point

interface Projection {

    fun toGame(x: Int, y: Int): Position?

    fun toScreen(
            localX: Int, localY: Int, height: Int, plane: Int,
            tileHeightLocalX: Int, tileHeightLocalY: Int,
            result: Point
    ): Boolean

    fun toScreen(
            localX: Int, localY: Int, height: Int, plane: Int,
            result: Point
    ): Boolean {
        return toScreen(localX, localY, height, plane, localX, localY, result)
    }

    fun toScreen(
            localX: Int, localY: Int, height: Int, plane: Int,
            tileHeightLocalX: Int, tileHeightLocalY: Int
    ): Point? {
        val pt = Point()
        return if (toScreen(localX, localY, height, plane, tileHeightLocalX, tileHeightLocalY, pt)) pt else null
    }

    fun toGame(point: Point): Position? {
        return toGame(point.x, point.y)
    }

    fun toScreen(position: Position): Point? {
        return toScreen(position, position)
    }

    fun toScreen(position: Position, tileHeight: Position): Point? {
        return toScreen(position.localX, position.localY, position.height, position.plane, tileHeight.localX, tileHeight.localY)
    }

    fun toScreen(localX: Int, localY: Int, height: Int, plane: Int): Point? {
        return toScreen(localX, localY, height, plane, localX, localY)
    }
}