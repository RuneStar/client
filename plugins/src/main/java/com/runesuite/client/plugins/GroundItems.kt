package com.runesuite.client.plugins

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.GlobalTile
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Game
import com.runesuite.client.game.live.Viewport
import java.awt.Color
import java.awt.Font
import com.runesuite.client.game.live.GroundItems as LiveGroundItems

class GroundItems : DisposablePlugin<GroundItems.Settings>() {

    override val defaultSettings = Settings()

    private lateinit var tiles: MutableSet<GlobalTile>

    override fun start() {
        super.start()

        tiles = HashSet<GlobalTile>()

        LiveGroundItems.pileRemovals.subscribe {
            tiles.remove(it.toGlobalTile())
        }
        LiveGroundItems.pileChanges.subscribe {
            tiles.add(it.toGlobalTile())
        }

        val font = Font.decode(null)

        Canvas.Live.repaints.subscribe { g ->
            g.color = Color.WHITE
            g.font = font
            val frc = g.fontRenderContext

            val itr = tiles.iterator()
            var gTile: GlobalTile
            val viewportShape = Viewport.Live.shape
            while (itr.hasNext()) {
                gTile = itr.next()
                if (gTile.plane != Game.plane) {
                    itr.remove()
                    continue
                }
                val sTile = gTile.toSceneTile()
                if (!sTile.isLoaded) {
                    itr.remove()
                    continue
                }
                val tilePt = sTile.center.toScreen()
                if (tilePt !in viewportShape) {
                    continue
                }
                val items = LiveGroundItems.getAt(sTile)
                if (items.isEmpty()) {
                    itr.remove()
                    continue
                }

                var currY = tilePt.y
                val centerX = tilePt.x
                for (item in items.asReversed()) {
                    val itemString = "${item.id} (${item.quantity})"
                    val stringRect = font.getStringBounds(itemString, frc).bounds
                    stringRect.y = currY
                    stringRect.x = centerX - stringRect.width.div(2)
                    if (stringRect !in viewportShape) {
                        break
                    }
                    g.drawString(itemString, stringRect.x, stringRect.y)
                    currY -= stringRect.height + 1
                }
            }
        }
    }

    class Settings: Plugin.Settings() {

    }
}