package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.Skills
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.util.ColorForm
import com.runesuite.client.plugins.util.FontForm
import java.awt.Font

class SkillsDebug : DisposablePlugin<SkillsDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            var y = 35
            var x = 5
            Skills.levels.entries.forEach { e ->
                g.drawString(e.toString(), x, y)
                y += g.font.size + 5
            }

            y = 35
            x = Canvas.Live.shape.width / 4
            Skills.currentLevels.entries.forEach { e ->
                g.drawString(e.toString(), x, y)
                y += g.font.size + 5
            }

            y = 35
            x = Canvas.Live.shape.width / 2
            Skills.experience.entries.forEach { e ->
                g.drawString(e.toString(), x, y)
                y += g.font.size + 5
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 14f)
        val color = ColorForm()
    }
}