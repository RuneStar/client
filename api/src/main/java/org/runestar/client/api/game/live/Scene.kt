package org.runestar.client.api.game.live

import io.reactivex.Observable
import org.runestar.client.api.game.GameState
import org.runestar.client.api.game.GlobalTile
import org.runestar.client.api.game.LocalValue
import org.runestar.client.api.game.Position
import org.runestar.client.api.game.Region
import org.runestar.client.api.game.SceneTile
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XScene

object Scene {

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

    fun getHeight(x: Int, y: Int, plane: Int): Int {
        return CLIENT.tiles_heights[plane][x][y]
    }

    fun getRenderFlags(x: Int, y: Int, plane: Int): Byte {
        return CLIENT.tiles_renderFlags[plane][x][y]
    }

    fun getCollisionFlags(x: Int, y: Int, plane: Int): Int {
        return CLIENT.collisionMaps[plane].flags[x][y]
    }

    val base get() = GlobalTile(CLIENT.baseX, CLIENT.baseY, 0)

    val regionIds: IntArray get() = CLIENT.regions ?: IntArray(0)

    val regions: List<Region> get() = regionIds.map { Region(it) }

    val reloads: Observable<Unit> = XScene.init.exit.map { Unit }

    fun reload() {
        if (Game.state == GameState.LOGGED_IN) CLIENT.updateGameState(GameState.LOADING)
    }

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

    override fun toString(): String {
        return "Scene($base)"
    }
}