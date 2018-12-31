package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.access.XHitSplatDefinition
import org.runestar.client.plugins.spi.PluginSettings

class HitSplatsX10 : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(XHitSplatDefinition.getString.exit.subscribe {
            if (it.returned != "0") {
                it.returned += '0'
            }
        })
    }
}