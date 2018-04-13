package org.runestar.client.plugins.dev

import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_LARGE_FONT

class OnTickTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    var tiles = ArrayList<SceneTile>()
    var tick = 0

    override fun start() {
        add(Game.ticks.subscribe {
            tiles = ArrayList()
            Players.mapTo(tiles) { it.location }
            Npcs.mapTo(tiles) { it.location }
            tick++
        })

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_LARGE_FONT
            tiles.forEach { t ->
                val o = t.outline()
                g.draw(o)
            }
            g.drawString(tick.toString(), 50, 50)
        })
    }
}