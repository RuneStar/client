package com.runesuite.client.api.live

import com.runesuite.client.api.GroundItem
import com.runesuite.client.api.SceneTile
import com.runesuite.client.raw.access.XGroundItem
import com.runesuite.client.raw.access.XScene
import com.runesuite.client.raw.access.XTile
import io.reactivex.Observable

object GroundItems : TileEntities.Many<GroundItem>() {

    override fun fromTile(sceneTile: SceneTile, xTile: XTile?): List<GroundItem> {
        val pile = xTile?.groundItemPile ?: return emptyList()
        val list = ArrayList<GroundItem>()
        var obj = pile.bottom
        while (obj is XGroundItem) {
            list.add(GroundItem(obj, sceneTile))
            obj = obj.previous as? XGroundItem
        }
        return list
    }

    /**
     * Item added or removed from tile, tile now has item(s)
     */
    val pileChanges: Observable<SceneTile> = XScene.setGroundItemPile.exit.map {
        SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }

    /**
     * Item removed from tile, tile now has no items
     */
    val pileRemovals: Observable<SceneTile> = XScene.removeGroundItemPile.exit.map {
        SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }
}