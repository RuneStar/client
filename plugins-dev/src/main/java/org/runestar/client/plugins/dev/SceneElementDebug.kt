package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawString
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.SceneElements
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Color

class SceneElementDebug : DisposablePlugin<SceneElementDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->

            g.font = RUNESCAPE_SMALL_FONT

            if (ctx.settings.game) {
                g.color = Color.CYAN
                SceneElements.Game.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (ctx.settings.wall) {
                g.color = Color.ORANGE
                SceneElements.Wall.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (ctx.settings.floor) {
                g.color = Color.GREEN
                SceneElements.Floor.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (ctx.settings.boundary) {
                g.color = Color.WHITE
                SceneElements.Boundary.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }
        })
    }

    data class Settings(
            val floor: Boolean = false,
            val wall: Boolean = true,
            val boundary: Boolean = true,
            val game: Boolean = true
    ) : PluginSettings()
}