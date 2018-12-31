package org.runestar.client.plugins.dev

import org.runestar.client.game.api.ProjectileCollision
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import java.awt.Color

class ProjectilePathTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->
            val me = Players.local?.location ?: return@subscribe
            g.color = Color.GREEN

            val pf = ProjectileCollision(LiveScene)
            Game.visibilityMap.visibleTiles().filter {
                me.distanceTo(it) <= 10 && pf.canReach(me, it)
            }.forEach { t ->
                g.draw(t.outline())
            }
        })
    }
}