package com.runesuite.client.game.api

import com.runesuite.client.game.api.live.Scene

data class GlobalTile(val x: Int, val y: Int, val plane: Int) {

    val region get() = com.runesuite.client.game.api.Region(x / com.runesuite.client.game.api.Region.Companion.SIZE, y / com.runesuite.client.game.api.Region.Companion.SIZE, plane)

    fun toSceneTile(scene: Scene = Scene.Live): com.runesuite.client.game.api.SceneTile {
        val base = scene.base
        return com.runesuite.client.game.api.SceneTile(x - base.x, y - base.y, plane)
    }

    fun isLoaded(scene: Scene = Scene.Live): Boolean {
        return toSceneTile(scene).isLoaded
    }
}
