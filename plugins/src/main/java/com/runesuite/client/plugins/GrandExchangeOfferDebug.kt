package com.runesuite.client.plugins

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.FileReadWriter
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.GrandExchangeOffers
import java.awt.Color

class GrandExchangeOfferDebug : DisposablePlugin<Plugin.Settings>(), FileReadWriter.Yaml<Plugin.Settings> {

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