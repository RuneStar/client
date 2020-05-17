package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.api.game.live.Inventory
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color

class InventoryDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE
            val cn = Inventory.container ?: return@subscribe
            cn.forEach { i ->
                g.drawString(i.toString(), x, y)
                y += g.font.size + 5
            }
        })
    }
}