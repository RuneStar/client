package org.runestar.client.plugins.dev

import org.runestar.client.game.api.Viewport
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class ViewportDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.CYAN
            g.draw(LiveViewport.shape)
            g.color = Color.BLUE
            g.draw(Viewport.Fixed.DEFAULT.shape)
            val strings = listOf(
                    "viewportOffsetX/Y: ${accessor.viewportOffsetX}, ${accessor.viewportOffsetY}",
                    "viewportWidth/Height: ${accessor.viewportWidth}, ${accessor.viewportHeight}",
                    "viewportZoom: ${accessor.viewportZoom}",
                    "viewportMouseX/Y: ${accessor.viewportMouse_x}, ${accessor.viewportMouse_y}",
                    "viewportContainsMouse: ${accessor.viewportMouse_isInViewport}"
            )
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE
            val x = 20
            var y = 40
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}