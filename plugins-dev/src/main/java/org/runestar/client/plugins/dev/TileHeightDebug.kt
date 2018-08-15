package org.runestar.client.plugins.dev

import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Scene
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class TileHeightDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE
            val fontSize = g.font.size

            for (x in 0 until Scene.SIZE) {
                for (y in 0 until Scene.SIZE) {
                    val tile = SceneTile(x, y, Game.plane)
                    val height = LiveScene.getHeight(tile)
                    val center = tile.center.toScreen()
                    if (center != null) {
                        g.drawString(height.toString(), center.x - fontSize, center.y - fontSize)
                    }
                }
            }
        })
    }
}