package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.access.XHitmarkType
import org.runestar.client.plugins.spi.PluginSettings

class HitmarksX10 : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(XHitmarkType.getString.exit.subscribe {
            if (it.returned != "0") {
                it.returned += '0'
            }
        })
    }
}