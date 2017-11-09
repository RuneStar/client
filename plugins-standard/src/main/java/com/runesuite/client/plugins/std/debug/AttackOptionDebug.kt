package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.AttackOptions
import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XAttackOption
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.util.ColorForm
import com.runesuite.client.plugins.util.FontForm
import java.awt.Color
import java.awt.Font

class AttackOptionDebug : DisposablePlugin<AttackOptionDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
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