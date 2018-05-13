package org.runestar.client.game.api

import org.runestar.client.game.api.live.LiveScene

data class GlobalTile(val x: Int, val y: Int, val plane: Int) {

    val region get() = Region(x / Region.SIZE, y / Region.SIZE)

    fun toSceneTile(scene: Scene = LiveScene): SceneTile {
        val base = scene.base
        return SceneTile(x - base.x, y - base.y, plane)
    }

    fun isLoaded(scene: Scene = LiveScene): Boolean {
        return toSceneTile(scene).isLoaded
    }
}
