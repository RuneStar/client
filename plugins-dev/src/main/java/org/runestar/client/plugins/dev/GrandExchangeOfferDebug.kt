package org.runestar.client.plugins.dev

import org.runestar.client.api.game.live.GrandExchangeOffers
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.api.plugins.DisposablePlugin
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