package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.GrandExchangeOffers
import org.runestar.client.game.api.live.Canvas
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import java.awt.Color

class GrandExchangeOfferDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
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