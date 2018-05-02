package org.runestar.client.game.api

/**
 * The loaded area that follows the local player.
 */
interface Scene {

    companion object {
        const val SIZE = 104
        const val PLANE_SIZE = 4
        val CENTER = SceneTile(SIZE / 2, SIZE / 2, 0)
        val BASE = SceneTile(0, 0, 0)
        val CORNERS = BASE.run {
            listOf(
                    this,
                    copy(x = SIZE - 1),
                    copy(x = SIZE - 1, y = SIZE - 1),
                    copy(y = SIZE - 1)
            )
        }
    }

    val base: GlobalTile

    fun getHeight(x: Int, y: Int, plane: Int): Int

    fun getRenderFlags(x: Int, y: Int, plane: Int): Byte

    fun getCollisionFlags(x: Int, y: Int, plane: Int): Int

    fun getHeight(sceneTile: SceneTile): Int {
        require(sceneTile.isLoaded) { sceneTile }
        return getHeight(sceneTile.x, sceneTile.y, sceneTile.plane)
    }

    fun isLinkBelow(x: Int, y: Int, plane: Int): Boolean {
        return plane < 3 && getRenderFlags(x, y, 1).toInt() and 2 != 0
    }

    fun isLinkBelow(sceneTile: SceneTile): Boolean {
        require(sceneTile.isLoaded) { sceneTile }
        return isLinkBelow(sceneTile.x, sceneTile.y, sceneTile.plane)
    }

    fun getTileHeight(position: Position): Int {
        require(position.isLoaded) { position }
        var p = position.plane
        if (isLinkBelow(position.x, position.y, position.plane)) {
            p++
        }
        val o = getHeight(position.x, position.y, p)
        val ne = if (position.x != SIZE - 1 && position.y != SIZE - 1) getHeight(1 + position.x, 1 + position.y, p) else o
        val n = if (position.y != SIZE - 1) getHeight(position.x, 1 + position.y, p) else o
        val e = if (position.x != SIZE - 1) getHeight(1 + position.x, position.y, p) else o
        return position.subY * (ne * position.subX + n * (128 - position.subX) shr 7) +
                (128 - position.subY) * (position.subX * e + o * (128 - position.subX) shr 7) shr 7
    }

    fun getRenderFlags(sceneTile: SceneTile): Byte {
        require(sceneTile.isLoaded) { sceneTile }
        return getRenderFlags(sceneTile.x, sceneTile.y, sceneTile.plane)
    }

    fun getCollisionFlags(sceneTile: SceneTile): Int {
        require(sceneTile.isLoaded) { sceneTile }
        return getCollisionFlags(sceneTile.x, sceneTile.y, sceneTile.plane)
    }
}