package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color
import java.awt.Font

class SelectionDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.SANS_SERIF, Font.BOLD, 15)

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = font
            g.color = Color.WHITE
            val strings = listOf(
                    "item: ${accessor.lastSelectedItemName}",
                    "spellAction: ${accessor.lastSelectedSpellActionName}",
                    "spellName: ${accessor.lastSelectedSpellName}",
                    "isItemSelected: ${accessor.isItemSelected}",
                    "inventorySlotHovered: ${accessor.inventorySlotHovered}",
                    "inventorySlotPressed: ${accessor.inventorySlotPressed}"
            )
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 20
            }
        })
    }
}