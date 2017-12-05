package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.Scene
import com.runesuite.client.game.api.SceneTile
import com.runesuite.client.game.api.live.Game
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.LiveScene
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.ColorForm
import com.runesuite.client.plugins.utils.DisposablePlugin
import com.runesuite.client.plugins.utils.FontForm
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
                    val pt = tile.center.toScreen() - Point(g.font.size, g.font.size)
                    g.drawString(height.toString(), pt)
                }
            }
        })
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}