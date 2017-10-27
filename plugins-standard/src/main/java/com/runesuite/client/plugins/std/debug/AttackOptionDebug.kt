package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XAttackOption
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color
import java.awt.Font

class AttackOptionDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.SANS_SERIF, Font.BOLD, 15)

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = font
            g.color = Color.WHITE
            val strings = listOf(
                    "player: ${attackOptionToString(accessor.playerAttackOption)}",
                    "npc: ${attackOptionToString(accessor.npcAttackOption)}"
            )
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 20
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
}