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
        return isLinkBelow(sceneTile.x, sceneTile.y, sceneTile.plane)
    }

    fun getTileHeight(position: Position): Int {
        return getTileHeight(position.localX, position.localY, position.plane)
    }

    fun getTileHeight(localX: Int, localY: Int, plane: Int): Int {
        val x = LocalValue(localX).scene
        val y = LocalValue(localY).scene
        require(SceneTile.isLoaded(x, y, plane))
        val subX = LocalValue(localX).sub
        val subY = LocalValue(localY).sub
        val p = if (isLinkBelow(x, y, plane)) {
            plane + 1
        } else {
            plane
        }
        val o = getHeight(x, y, p)
        if (x == SIZE - 1 || y == SIZE - 1) return o
        val ne = getHeight(1 + x, 1 + y, p)
        val n = getHeight(x, 1 + y, p)
        val e = getHeight(1 + x, y, p)
        return subY * (ne * subX + n * (128 - subX) shr 7) +
                (128 - subY) * (subX * e + o * (128 - subX) shr 7) shr 7
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