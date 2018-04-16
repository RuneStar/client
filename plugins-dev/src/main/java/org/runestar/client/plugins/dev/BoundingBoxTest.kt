package org.runestar.client.plugins.dev

import com.google.common.collect.Iterables
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.api.live.Projectiles
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color

class BoundingBoxTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.WHITE

            val actors = Iterables.concat(Npcs, Players)
            actors.forEach { actor ->
                val m = actor.model ?: return@forEach
                m.drawBoundingBox(g)
            }

            Projectiles.forEach { p ->
                val m = p.model ?: return@forEach
                m.drawBoundingBox(g)
            }
        })
    }
}