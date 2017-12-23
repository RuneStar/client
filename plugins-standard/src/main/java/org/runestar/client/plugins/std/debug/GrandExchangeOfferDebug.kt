package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.GrandExchangeOffers
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.plugins.utils.DisposablePlugin
import java.awt.Color

class GrandExchangeOfferDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.color = Color.WHITE
            GrandExchangeOffers.get().forEach { o ->
                g.drawString(o.toString(), x, y)
                y += 13
            }
        })
    }
}