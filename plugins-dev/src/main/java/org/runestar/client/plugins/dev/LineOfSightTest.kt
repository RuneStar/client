package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.LineOfSight
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Players
import org.runestar.client.api.game.live.VisibilityMap
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color

class LineOfSightTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            val me = Players.local?.location ?: return@subscribe
            g.color = Color.GREEN

            for (t in VisibilityMap.visibleTiles()) {
                if (me.distanceTo(t) <= 10 && LineOfSight.canReach(me, t)) {
                    g.draw(t.outline())
                }
            }
        })
    }
}