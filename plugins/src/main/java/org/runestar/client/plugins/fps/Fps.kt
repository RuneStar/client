package org.runestar.client.plugins.fps

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class Fps : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "FPS"

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.YELLOW
            g.font = RUNESCAPE_CHAT_FONT

            g.drawStringShadowed(
                    Client.accessor.fps.toString(),
                    Client.accessor.canvasWidth - 43,
                    g.fontMetrics.height
            )
        })
    }
}