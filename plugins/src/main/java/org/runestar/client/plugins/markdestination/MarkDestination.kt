package org.runestar.client.plugins.markdestination

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.BasicStroke
import java.awt.Color
import java.awt.RenderingHints

class MarkDestination : DisposablePlugin<PluginSettings>() {

    private companion object {
        val STROKE = BasicStroke(2.0f)
    }

    override val defaultSettings = PluginSettings()

    override val name = "Mark Destination"

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val destination = Game.destination ?: return@subscribe
            val outline = destination.outline()
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.color = Color.GRAY
            g.stroke = STROKE
            g.draw(outline)
        })
    }
}