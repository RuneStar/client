package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.GrandExchangeOffers
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin
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