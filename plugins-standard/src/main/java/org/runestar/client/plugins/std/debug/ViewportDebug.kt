package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.Viewport
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import java.awt.Color
import java.awt.Font

class ViewportDebug : DisposablePlugin<ViewportDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
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
            g.font = settings.font.get()
            g.color = settings.color.get()
            val x = 20
            var y = 40
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}