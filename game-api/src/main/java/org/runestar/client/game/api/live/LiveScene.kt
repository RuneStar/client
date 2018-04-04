package org.runestar.client.game.api.live

import org.runestar.client.game.api.GlobalTile
import org.runestar.client.game.api.Scene
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.raw.Client.accessor

object LiveScene : Scene {

    override fun getCollisionFlags(sceneTile: SceneTile): Int {
        require(sceneTile.isLoaded) { sceneTile }
        return accessor.collisionMaps[sceneTile.plane].flags[sceneTile.x][sceneTile.y]
    }

    override fun getCollisionFlags(plane: Int): Array<IntArray> {
        require(plane in 0 until Scene.PLANE_SIZE) { plane }
        return accessor.collisionMaps[plane].flags
    }

    override val collisionFlags get() = accessor.collisionMaps.map { it.flags }.toTypedArray()

    override val renderFlags get() = accessor.tiles_renderFlags

    override val heights get() = accessor.tiles_heights

    override val base get() = GlobalTile(accessor.baseX, accessor.baseY, 0)

    override fun toString(): String {
        return "LiveScene(base=$base)"
    }
}