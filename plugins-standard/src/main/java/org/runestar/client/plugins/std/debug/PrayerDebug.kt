package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.Prayer
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Prayers
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class PrayerDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE
            val strings = ArrayList<String>()
            strings.add(Prayers.level.toString())
            Prayer.values().filter { Prayers.isEnabled(it) }.mapTo(strings) { it.toString() }
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}