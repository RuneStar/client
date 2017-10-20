package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.Minimap
import com.runesuite.client.game.api.live.Viewport
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color

class ViewportCanvasMinimapOutlineDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            g.color = Color.CYAN
            g.draw(Viewport.Live.shape)
            g.color = Color.BLUE
            g.draw(Viewport.Fixed.DEFAULT.shape)
            g.color = Color.ORANGE
            g.draw(Canvas.Live.shape)
            g.color = Color.RED
            g.draw(Canvas.Fixed.shape)
            g.color = Color.WHITE
            g.draw(Minimap.Live.circle)
        })
    }
}