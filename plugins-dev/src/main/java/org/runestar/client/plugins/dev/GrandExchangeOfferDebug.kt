package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.GrandExchangeOffers
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color

class GrandExchangeOfferDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.color = Color.WHITE
            GrandExchangeOffers.forEach { o ->
                g.drawString(o.toString(), x, y)
                y += 13
            }
        })
    }
}