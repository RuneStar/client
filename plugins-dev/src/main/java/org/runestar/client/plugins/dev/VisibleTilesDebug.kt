package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawPoint
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.VisibilityMap
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
import java.awt.BasicStroke
import java.awt.Color

class VisibleTilesDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        val stroke2 = BasicStroke(2f)
        add(Canvas.repaints.subscribe { g ->
            g.color = Color.RED
            VisibilityMap.visibleTiles().forEach { t ->
                g.draw(t.outline())
            }

            g.stroke = stroke2
            CLIENT.visibleTiles?.forEachIndexed { x, ys ->
                ys.forEachIndexed { y, b ->
                    g.color = if (b) Color.RED else Color.BLUE
                    g.drawPoint(x * 2, 102 - y * 2)
                }
            }
        })
    }
}