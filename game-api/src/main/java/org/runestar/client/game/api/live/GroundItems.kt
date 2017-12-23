package org.runestar.client.game.api.live

import org.runestar.client.game.api.GroundItem
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.raw.access.XGroundItem
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile
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
    val pileChanges: Observable<SceneTile> = XScene.newGroundItemPile.exit.map {
        SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }

    /**
     * Item removed from tile, tile now has no items
     */
    val pileRemovals: Observable<SceneTile> = XScene.removeGroundItemPile.exit.map {
        SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }
}