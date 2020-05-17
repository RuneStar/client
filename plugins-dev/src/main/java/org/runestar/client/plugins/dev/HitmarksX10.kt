package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.raw.access.XHitmarkType
import org.runestar.client.api.plugins.PluginSettings

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