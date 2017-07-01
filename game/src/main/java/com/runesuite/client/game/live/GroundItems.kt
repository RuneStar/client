package com.runesuite.client.game.live

import com.runesuite.client.base.access.XGroundItem
import com.runesuite.client.base.access.XTile
import com.runesuite.client.game.GroundItem
import com.runesuite.client.game.SceneTile

object GroundItems : SceneObjects.Base.Many<GroundItem>() {

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
}