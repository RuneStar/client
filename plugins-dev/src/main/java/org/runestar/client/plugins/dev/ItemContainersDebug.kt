package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Fonts
import org.runestar.client.game.api.live.ItemContainers
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class ItemContainersDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = Fonts.CHAT
            g.color = Color.WHITE

            val strings = ArrayList<String>()
            ItemContainers.forEach { k, v ->
                val vs = v.map { it?.let { "(${it.id}x${it.quantity})" } }
                strings.add("$k:$vs")
            }

            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}