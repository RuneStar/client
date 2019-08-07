package org.runestar.client.plugins.dev

import org.kxtra.swing.graphics.drawString
import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.SceneElements
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class SceneElementDebug : DisposablePlugin<SceneElementDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->

            g.font = Fonts.PLAIN_11

            if (settings.scenery) {
                g.color = Color.CYAN
                SceneElements.Scenery.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (settings.wallDecoration) {
                g.color = Color.ORANGE
                SceneElements.WallDecoration.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (settings.floorDecoration) {
                g.color = Color.GREEN
                SceneElements.FloorDecoration.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (settings.wall) {
                g.color = Color.WHITE
                SceneElements.Wall.onPlane(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }
        })
    }

    data class Settings(
            val floorDecoration: Boolean = false,
            val wallDecoration: Boolean = true,
            val wall: Boolean = true,
            val scenery: Boolean = true
    ) : PluginSettings()
}