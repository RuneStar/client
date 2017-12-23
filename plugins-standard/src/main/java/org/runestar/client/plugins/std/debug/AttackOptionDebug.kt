package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.AttackOptions
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.plugins.utils.ColorForm
import org.runestar.client.plugins.utils.DisposablePlugin
import org.runestar.client.plugins.utils.FontForm
import java.awt.Font

class AttackOptionDebug : DisposablePlugin<AttackOptionDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = settings.font.get()
            g.color = settings.color.get()
            val strings = listOf(
                    "player: ${AttackOptions.player}",
                    "npc: ${AttackOptions.npc}"
            )
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