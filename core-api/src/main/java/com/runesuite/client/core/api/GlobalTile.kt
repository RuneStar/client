package com.runesuite.client.core.api

import com.runesuite.client.core.api.live.Scene

data class GlobalTile(val x: Int, val y: Int, val plane: Int) {

    val region get() = com.runesuite.client.core.api.Region(x / com.runesuite.client.core.api.Region.Companion.SIZE, y / com.runesuite.client.core.api.Region.Companion.SIZE, plane)

    fun toSceneTile(scene: Scene = Scene.Live): com.runesuite.client.core.api.SceneTile {
        val base = scene.base
        return com.runesuite.client.core.api.SceneTile(x - base.x, y - base.y, plane)
    }

    fun isLoaded(scene: Scene = Scene.Live): Boolean {
        return toSceneTile(scene).isLoaded
    }
}
