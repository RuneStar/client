package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.api.game.EnumId
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Enums
import org.runestar.client.api.game.live.Prayers
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color

class PrayerDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE
            val strings = ArrayList<String>()
            val prayers = Enums[EnumId.PRAYER_NAMES].keys
            prayers.filter { Prayers.enabled[it] }.mapTo(strings) { Prayers.name(it) }
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}