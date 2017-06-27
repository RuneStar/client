package com.runesuite.client.game.live

import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XGroundItem
import com.runesuite.client.game.GroundItem
import com.runesuite.client.game.SceneTile

object GroundItems {

    operator fun get(sceneTile: SceneTile): List<GroundItem> {
        require(sceneTile.isLoaded) { sceneTile }
        val tile = Client.accessor.scene.tiles[sceneTile.plane][sceneTile.x][sceneTile.y] ?: return emptyList()
        val pile = tile.groundItemPile ?: return emptyList()
        val items = ArrayList<GroundItem>()
        var item = pile.bottom as? XGroundItem?
        while (item is XGroundItem) {
            items.add(GroundItem(item, sceneTile))
            item = item.previous as? XGroundItem
        }
        return items
    }

    operator fun get(plane: Int): List<List<List<GroundItem>>> {
        require(plane in 0 until Scene.PLANE_SIZE) { plane }
        val items = ArrayList<ArrayList<List<GroundItem>>>(Scene.SIZE)
        for (x in 0 until Scene.SIZE) {
            items.add(ArrayList<List<GroundItem>>(Scene.SIZE))
            for (y in 0 until Scene.SIZE) {
                val tile = SceneTile(x, y, plane)
                items[x][y] = get(tile)
            }
        }
        return items
    }
}