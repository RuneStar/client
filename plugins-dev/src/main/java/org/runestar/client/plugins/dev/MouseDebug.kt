package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class MouseDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->
            val x = 5
            var y = 40
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE
            val strings = ArrayList<String>()

            strings.apply {
                add("mouse")
                add("location: ${Mouse.location}")
                add("viewportLocation: ${Mouse.viewportLocation}")
                add("cross: ${Mouse.cross}")
                add("isInViewport: ${Mouse.isInViewport}")
                add("entityCount: ${Mouse.entityCount}")
                add("tags:")
                Mouse.tags.mapTo(this) { it.toString() }
            }

            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}