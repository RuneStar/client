package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.ProjectileCollision
import org.runestar.client.game.api.live.Canvas
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.api.live.VisibilityMap
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class ProjectilePathTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            val me = Players.local?.location ?: return@subscribe
            g.color = Color.GREEN

            val pf = ProjectileCollision
            VisibilityMap.visibleTiles().filter {
                me.distanceTo(it) <= 10 && pf.canReach(me, it)
            }.forEach { t ->
                g.draw(t.outline())
            }
        })
    }
}