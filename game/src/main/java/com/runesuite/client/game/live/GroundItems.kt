package com.runesuite.client.game.live

import com.runesuite.client.base.access.XGroundItem
import com.runesuite.client.base.access.XScene
import com.runesuite.client.base.access.XTile
import com.runesuite.client.game.GroundItem
import com.runesuite.client.game.SceneTile
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

    val spawns: Observable<SceneTile> = XScene.newGroundItemPile.exit.map {
        SceneTile(it.arguments[1] as Int, it.arguments[2] as Int, it.arguments[0] as Int)
    }
}