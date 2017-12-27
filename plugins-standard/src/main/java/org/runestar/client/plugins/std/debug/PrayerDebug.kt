package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.Prayer
import org.runestar.client.game.api.live.AttackOptions
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Prayers
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
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