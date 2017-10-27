package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Camera
import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color
import java.awt.Font

class LastSelectedDebug : DisposablePlugin<PluginSettings>() {

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
                    "item: ${accessor.lastSelectedItemName}",
                    "spellAction: ${accessor.lastSelectedSpellActionName}",
                    "spellName: ${accessor.lastSelectedSpellName}"
            )
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 20
            }
        })
    }
}