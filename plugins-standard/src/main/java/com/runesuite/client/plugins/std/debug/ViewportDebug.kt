package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.Viewport
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color
import java.awt.Font

class ViewportDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.MONOSPACED, Font.BOLD, 13)

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            g.color = Color.CYAN
            g.draw(Viewport.Live.shape)
            g.color = Color.BLUE
            g.draw(Viewport.Fixed.DEFAULT.shape)
            val strings = listOf(
                    "    viewportOffsetX/Y: ${accessor.viewportOffsetX}, ${accessor.viewportOffsetY}",
                    " viewportWidth/Height: ${accessor.viewportWidth}, ${accessor.viewportHeight}",
                    "         viewportZoom: ${accessor.viewportZoom}",
                    "     viewportMouseX/Y: ${accessor.viewportMouseX}, ${accessor.viewportMouseY}",
                    "viewportContainsMouse: ${accessor.viewportContainsMouse}"
            )
            g.font = font
            g.color = Color.WHITE
            val x = 20
            var y = 40
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 20
            }
        })
    }
}