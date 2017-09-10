package com.runesuite.client.core.api.live

import com.runesuite.client.core.api.SceneTile
import com.runesuite.client.core.raw.access.XGroundItem
import com.runesuite.client.core.raw.access.XScene
import com.runesuite.client.core.raw.access.XTile
import io.reactivex.Observable

object GroundItems : TileEntities.Many<com.runesuite.client.core.api.GroundItem>() {

    override fun fromTile(sceneTile: com.runesuite.client.core.api.SceneTile, xTile: XTile?): List<com.runesuite.client.core.api.GroundItem> {
        val pile = xTile?.groundItemPile ?: return emptyList()
        val list = ArrayList<com.runesuite.client.core.api.GroundItem>()
        var obj = pile.bottom
        while (obj is XGroundItem) {
            list.add(com.runesuite.client.core.api.GroundItem(obj, sceneTile))
            obj = obj.previous as? XGroundItem
        }
        return list
    }

    /**
     * Item added or removed from tile, tile now has item(s)
     */
    val pileChanges: Observable<SceneTile> = XScene.setGroundItemPile.exit.map {
        com.runesuite.client.core.api.SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }

    /**
     * Item removed from tile, tile now has no items
     */
    val pileRemovals: Observable<com.runesuite.client.core.api.SceneTile> = XScene.removeGroundItemPile.exit.map {
        com.runesuite.client.core.api.SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }
}