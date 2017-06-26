package com.runesuite.client.plugins

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.FileReadWriter
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Widgets
import java.awt.Color

class WidgetDebug : DisposablePlugin<Plugin.Settings>(), FileReadWriter.Yaml<Plugin.Settings> {

    override val defaultSettings = Plugin.Settings()

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            g.color = Color.WHITE
            Widgets.all.forEach { w ->
                w.shape?.let { s ->
                    g.draw(s)
                }
            }
        })
    }
}