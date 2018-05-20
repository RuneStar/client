package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.Inventory
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class InventoryDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE
            val cn = Inventory.container ?: return@subscribe
            cn.forEach { i ->
                g.drawString(i.toString(), x, y)
                y += g.font.size + 5
            }
        })
    }
}