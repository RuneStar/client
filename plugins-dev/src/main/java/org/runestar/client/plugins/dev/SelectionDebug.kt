package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.ComponentId
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color
import java.awt.Font

class SelectionDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.SANS_SERIF, Font.BOLD, 15)

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = font
            g.color = Color.WHITE
            val strings = listOf(
                    "item: ${CLIENT.selectedItemName}",
                    "isSpellSelected: ${CLIENT.isSpellSelected}",
                    "spellAction: ${CLIENT.selectedSpellActionName}",
                    "spellName: ${CLIENT.selectedSpellName}",
                    "isItemSelected: ${CLIENT.isItemSelected}",
                    "selectedItemId: ${CLIENT.selectedItemId}",
                    "selectedItemSlot: ${CLIENT.selectedItemSlot}",
                    "selectedItemComponent: ${CLIENT.selectedItemComponent}",
                    "itemDragDuration: ${CLIENT.itemDragDuration}",
                    "isDraggingComponent: ${CLIENT.isDraggingComponent}",
                    "componentDragDuration: ${CLIENT.componentDragDuration}",
                    "dragItemSlotSource: ${CLIENT.dragItemSlotSource}",
                    "dragItemSlotDestination: ${CLIENT.dragItemSlotDestination}",
                    "dragInventoryComponent: ${CLIENT.dragInventoryComponent?.let { ComponentId(it.id) }}"
            )
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 20
            }
        })
    }
}