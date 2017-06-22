package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.GlobalTile
import com.runesuite.client.game.Position
import com.runesuite.client.game.SceneTile

/**
 * The loaded area that follows the local player.
 */
interface Scene {

    companion object {
        const val SIZE = 104
        const val PLANE_SIZE = 4
        val CENTER = SceneTile(SIZE / 2, SIZE / 2, 0)
        val BASE = SceneTile(0, 0, 0)
        val CORNERS = BASE.run { listOf(
                this,
                copy(x = SIZE - 1),
                copy(x = SIZE - 1, y = SIZE - 1),
                copy(y = SIZE - 1)) }
    }

    val base: GlobalTile

    fun getHeight(sceneTile: SceneTile): Int

    fun getTileHeight(position: Position): Int {
        require(position.isLoaded) { position }
        var p = position.plane
        if (p < 3 && 0 != (getRenderFlags(SceneTile(position.x, position.y, 1)).toInt() and 2)) {
            p++
        }
        val o = getHeight(SceneTile(position.x, position.y, p))
        val ne = if (position.x != SIZE - 1 && position.y != SIZE - 1) getHeight(SceneTile(1 + position.x, 1 + position.y, p)) else o
        val n = if (position.y != SIZE - 1) getHeight(SceneTile(position.x, 1 + position.y, p)) else o
        val e = if (position.x != SIZE - 1) getHeight(SceneTile(1 + position.x, position.y, p)) else o
        return position.subY * (ne * position.subX + n * (128 - position.subX) shr 7) +
                (128 - position.subY) * (position.subX * e + o * (128 - position.subX) shr 7) shr 7
    }

    fun getRenderFlags(sceneTile: SceneTile): Byte

    fun getCollisionFlags(sceneTile: SceneTile): Int

    object Live : Scene {

        override fun getCollisionFlags(sceneTile: SceneTile): Int {
            require(sceneTile.isLoaded) { sceneTile }
            return accessor.collisionMaps[sceneTile.plane].flags[sceneTile.x][sceneTile.y]
        }

        override fun getRenderFlags(sceneTile: SceneTile): Byte {
            require(sceneTile.isLoaded) { sceneTile }
            return accessor.tileRenderFlags[sceneTile.plane][sceneTile.x][sceneTile.y]
        }

        override fun getHeight(sceneTile: SceneTile): Int {
            require(sceneTile.isLoaded) { sceneTile }
            return accessor.tileHeights[sceneTile.plane][sceneTile.x][sceneTile.y]
        }

        override val base get() = GlobalTile(accessor.baseX, accessor.baseY)

        override fun toString(): String {
            return "Scene.Live(base=$base)"
        }

        fun copyOf(): Copy = Copy(base,
                accessor.tileRenderFlags.map { it.map { it.toList() } },
                accessor.tileHeights.map { it.map { it.toList() } },
                accessor.collisionMaps.map { it.flags.map { it.toList() } })
    }

    data class Copy(
            override val base: GlobalTile,
            val renderFlags: List<List<List<Byte>>>,
            val heights: List<List<List<Int>>>,
            val collisionFlags: List<List<List<Int>>>
    ) : Scene {

        override fun getCollisionFlags(sceneTile: SceneTile): Int {
            require(sceneTile.isLoaded) { sceneTile }
            return collisionFlags[sceneTile.plane][sceneTile.x][sceneTile.y]
        }

        override fun getHeight(sceneTile: SceneTile): Int {
            require(sceneTile.isLoaded) { sceneTile }
            return heights[sceneTile.plane][sceneTile.x][sceneTile.y]
        }

        override fun getRenderFlags(sceneTile: SceneTile): Byte {
            require(sceneTile.isLoaded) { sceneTile }
            return renderFlags[sceneTile.plane][sceneTile.x][sceneTile.y]
        }

        override fun toString(): String {
            return "Scene.Copy(base=$base)"
        }
    }
}