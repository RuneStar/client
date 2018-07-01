package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings

class FlattenTerrain : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(XClient.loadTerrain.exit.subscribe {
            CLIENT.tiles_heights[0].forEach {
                it.fill(0)
            }
        })
    }
}