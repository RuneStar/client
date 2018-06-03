package org.runestar.client.game.api.live

import io.reactivex.Observable
import org.runestar.client.game.api.GroundItem
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.TileObjects
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XGroundItem
import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.game.raw.access.XTile
import java.util.*

// todo
object GroundItems : TileObjects.Many<GroundItem>(Client.accessor.scene) {

    override fun fromTile(tile: XTile): Iterator<GroundItem> {
        val pile = tile.groundItemPile ?: return Collections.emptyIterator()
        val sceneTile = SceneTile(tile.x, tile.y, tile.plane)
        return object : AbstractIterator<GroundItem>() {

            private var cur: XNode? = pile.first

            override fun computeNext() {
                val gi = cur as? XGroundItem ?: return done()
                setNext(GroundItem(gi, sceneTile.center))
                cur = gi.previous
            }
        }
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