package org.runestar.client.plugins.fps

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.api.Fonts
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class Fps : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "FPS"

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.YELLOW
            g.font = Fonts.PLAIN_12

            g.drawStringShadowed(
                    CLIENT.fps.toString(),
                    CLIENT.canvasWidth - 43,
                    g.fontMetrics.ascent + 1
            )
        })
    }
}