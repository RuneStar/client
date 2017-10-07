package com.runesuite.client.plugins.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.GrandExchangeOffers
import com.runesuite.client.pluginframework.DisposablePlugin
import com.runesuite.client.pluginframework.Plugin
import java.awt.Color

class GrandExchangeOfferDebug : DisposablePlugin<Plugin.Settings>() {

    override val defaultSettings = Plugin.Settings()

    override fun start() {
        super.start()
        Canvas.Live.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.color = Color.WHITE
            GrandExchangeOffers.get().forEach { o ->
                g.drawString(o.toString(), x, y)
                y += 13
            }
        }
    }
}