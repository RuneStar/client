package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.Viewport
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color

class ViewportDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            g.color = Color.CYAN
            g.draw(Viewport.shape)
            g.color = Color.BLUE
            val strings = listOf(
                    "viewportOffsetX/Y: ${CLIENT.viewportOffsetX}, ${CLIENT.viewportOffsetY}",
                    "viewportWidth/Height: ${CLIENT.viewportWidth}, ${CLIENT.viewportHeight}",
                    "viewportZoom: ${CLIENT.viewportZoom}",
                    "viewportMouseX/Y: ${CLIENT.viewportMouse_x}, ${CLIENT.viewportMouse_y}",
                    "viewportContainsMouse: ${CLIENT.viewportMouse_isInViewport}"
            )
            g.font = Fonts.PLAIN_12
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