package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.*
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin
import java.awt.Color

class PathFindTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()

        add(LiveCanvas.repaints.subscribe { g ->

            g.color = Color.BLUE

            val mouseTile = Projections.viewport.toGame(Mouse.location)?.sceneTile ?: return@subscribe
            val selfTile = Players.local?.location ?: return@subscribe

            val path = PathFinding.walking.findPath(selfTile, mouseTile) ?: return@subscribe

            path.forEach { st ->
                g.draw(st.outline())
            }
        })
    }
}