package org.runestar.client.plugins.inventorygrid

import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Components
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings

class InventoryGrid : DisposablePlugin<InventoryGrid.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Inventory Grid"

    override fun onStart() {
        val color = settings.color.value
        add(Canvas.repaints.subscribe { g ->
            val inv = Components.dragInventory ?: return@subscribe
            if (CLIENT.itemDragDuration <= 5) return@subscribe
            if (!inv.isActive) return@subscribe
            g.color = color
            for ((item, shape) in inv.items) {
                if (item == null) g.fill(shape)
            }
        })
    }

    class Settings(
            val color: RgbaForm = RgbaForm(255, 255, 255, 30)
    ) : PluginSettings()
}