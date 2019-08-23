package org.runestar.client.game.api

import org.runestar.client.game.api.live.Scene

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

    fun toScene(): SceneRectangle {
        val base = Scene.base
        return SceneRectangle(x - base.x, y - base.y, width, height, plane)
    }

    val base: GlobalTile get() = GlobalTile(x, y, plane)

    fun isLoaded(): Boolean {
        val base = Scene.base
        return SceneTile.isXyLoaded(x - base.x) &&
                SceneTile.isXyLoaded(y - base.y) &&
                SceneTile.isXyLoaded(x - base.x + width - 1) &&
                SceneTile.isXyLoaded(y - base.y + height - 1)
    }
}