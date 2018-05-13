package org.runestar.client.game.api

import org.runestar.client.game.api.live.LiveScene

data class GlobalRectangle(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int,
        val plane: Int
) {

    init {
        require(width > 0 && height > 0 && SceneTile.isPlaneLoaded(plane))
    }

    fun toScene(scene: Scene = LiveScene): SceneRectangle {
        val base = scene.base
        return SceneRectangle(x - base.x, y - base.y, width, height, plane)
    }

    val base: GlobalTile get() = GlobalTile(x, y, plane)

    fun isLoaded(scene: Scene = LiveScene): Boolean {
        val base = scene.base
        return SceneTile.isXyLoaded(x - base.x) &&
                SceneTile.isXyLoaded(y - base.y) &&
                SceneTile.isXyLoaded(x - base.x + width - 1) &&
                SceneTile.isXyLoaded(y - base.y + height - 1)
    }
}