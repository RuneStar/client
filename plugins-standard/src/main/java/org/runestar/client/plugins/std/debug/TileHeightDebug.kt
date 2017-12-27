package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.Scene
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import org.kxtra.swing.graphics2d.drawString
import org.kxtra.swing.point.minus
import java.awt.Font
import java.awt.Point

class TileHeightDebug : DisposablePlugin<TileHeightDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            for (x in 0 until Scene.SIZE) {
                for (y in 0 until Scene.SIZE) {
                    val tile = SceneTile(x, y, Game.plane)
                    val height = LiveScene.getHeight(tile)
                    val center = tile.center.toScreen()
                    if (center != null) {
                        val pt = center - Point(g.font.size, g.font.size)
                        g.drawString(height.toString(), pt)
                    }
                }
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}