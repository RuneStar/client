package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.api.game.SceneTile
import org.runestar.client.api.game.live.Game
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Npcs
import org.runestar.client.api.game.live.Players
import org.runestar.client.api.plugins.PluginSettings

class OnTickTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    var tiles = ArrayList<SceneTile>()
    var tick = 0

    override fun onStart() {
        add(Game.ticks.subscribe {
            tiles = ArrayList()
            Players.mapTo(tiles) { it.location }
            Npcs.mapTo(tiles) { it.location }
            tick++
        })

        add(Canvas.repaints.subscribe { g ->
            g.font = Fonts.BOLD_12
            tiles.forEach { t ->
                val o = t.outline()
                g.draw(o)
            }
            g.drawString(tick.toString(), 50, 50)
        })
    }
}