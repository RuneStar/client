package com.runesuite.client.game.live

import com.runesuite.client.base.Client
import com.runesuite.client.game.GameObject
import com.runesuite.client.game.SceneTile

object GameObjects {

    fun getAt(sceneTile: SceneTile): List<GameObject> {
        require(sceneTile.isLoaded) { sceneTile }
        val tile = Client.accessor.scene.tiles[sceneTile.plane][sceneTile.x][sceneTile.y] ?: return emptyList()
        val gos = tile.gameObjects ?: return emptyList()
        return gos.distinct().mapNotNull { it?.let { GameObject(it, sceneTile) } }
    }

    fun getOnPlane(plane: Int): List<List<List<GameObject>>> {
        require(plane in 0 until Scene.PLANE_SIZE) { plane }
        val items = ArrayList<ArrayList<List<GameObject>>>(Scene.SIZE)
        for (x in 0 until Scene.SIZE) {
            items.add(ArrayList<List<GameObject>>(Scene.SIZE))
            for (y in 0 until Scene.SIZE) {
                val tile = SceneTile(x, y, plane)
                items[x].add(getAt(tile))
            }
        }
        return items
    }

    fun getOnPlaneFlat(plane: Int): List<GameObject> {
        return getOnPlane(plane).flatMap { it.flatMap { it } }
    }
}