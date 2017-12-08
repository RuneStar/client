package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.Players
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.ColorForm
import com.runesuite.client.plugins.utils.DisposablePlugin
import com.runesuite.client.plugins.utils.FontForm
import java.awt.Font

class TestPlugin : DisposablePlugin<TestPlugin.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            val ap = Players.local?.appearance ?: return@subscribe

            var x = 5
            var y = 40
            ap.equipment.entries.forEach { e ->
                g.drawString(e.toString(), x ,y)
                y += g.font.size + 5
            }

            x = 150
            y = 40
            ap.kit.entries.forEach { e ->
                g.drawString(e.toString(), x ,y)
                y += g.font.size + 5
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}