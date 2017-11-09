package com.runesuite.client.plugins.std.debug

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
                    "player: ${attackOptionToString(accessor.playerAttackOption)}",
                    "npc: ${attackOptionToString(accessor.npcAttackOption)}"
            )
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }

    private fun attackOptionToString(attackOption: XAttackOption?): String {
        return when (attackOption) {
            accessor.attackOption_alwaysRightClick -> "alwaysRightClick"
            accessor.attackOption_dependsOnCombatLevels -> "dependsOnCombatLevels"
            accessor.attackOption_hidden -> "hidden"
            accessor.attackOption_leftClickWhereAvailable -> "leftClickWhereAvailable"
            null -> "null"
            else -> error(attackOption)
        }
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}