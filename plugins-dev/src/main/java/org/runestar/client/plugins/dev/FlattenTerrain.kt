package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class FlattenTerrain : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(XClient.loadTerrain.exit.subscribe {
            CLIENT.tiles_heights[0].forEach {
                it.fill(0)
            }
        })
    }
}