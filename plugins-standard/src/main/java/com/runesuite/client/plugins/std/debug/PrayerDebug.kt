package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.Prayer
import com.runesuite.client.game.api.live.AttackOptions
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.Prayers
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.ColorForm
import com.runesuite.client.plugins.utils.DisposablePlugin
import com.runesuite.client.plugins.utils.FontForm
import java.awt.Font

class PrayerDebug : DisposablePlugin<PrayerDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = settings.font.get()
            g.color = settings.color.get()
            val strings = ArrayList<String>()
            strings.add(Prayers.level.toString())
            Prayer.values().filter { Prayers.isEnabled(it) }.mapTo(strings) { it.toString() }
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}