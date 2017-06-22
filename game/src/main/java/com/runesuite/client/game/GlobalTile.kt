package com.runesuite.client.game

import com.runesuite.client.game.live.Scene

data class GlobalTile(val x: Int, val y: Int, val plane: Int = 0) {

    val region get() = Region(x / Region.SIZE, y / Region.SIZE, plane)

    fun toSceneTile(scene: Scene = Scene.Live): SceneTile {
        val base = scene.base
        return SceneTile(x - base.x, y - base.y, plane)
    }

    fun isLoaded(scene: Scene = Scene.Live): Boolean {
        return toSceneTile(scene).isLoaded
    }
}
