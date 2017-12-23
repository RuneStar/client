package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Stats
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.plugins.utils.ColorForm
import org.runestar.client.plugins.utils.DisposablePlugin
import org.runestar.client.plugins.utils.FontForm
import org.runestar.general.Skill
import java.awt.Font

class SkillsDebug : DisposablePlugin<SkillsDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            var y = 35
            val x = 5
            Skill.values().map { "$it ${Stats[it]}" }.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 14f)
        val color = ColorForm()
    }
}