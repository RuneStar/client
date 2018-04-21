package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawPoint
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.BasicStroke
import java.awt.Color

class VisibleTilesDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        val stroke2 = BasicStroke(2f)
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.RED
            Game.visibilityMap.visibleTiles().forEach { t ->
                g.draw(t.outline())
            }

            g.stroke = stroke2
            Client.accessor.visibleTiles?.forEachIndexed { x, ys ->
                ys.forEachIndexed { y, b ->
                    g.color = if (b) Color.RED else Color.BLUE
                    g.drawPoint(x * 2, 102 - y * 2)
                }
            }
        })
    }
}