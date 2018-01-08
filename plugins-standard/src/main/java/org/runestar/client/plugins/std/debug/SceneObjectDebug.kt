package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.SceneObjects
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.kxtra.swing.graphics2d.drawString
import java.awt.Color

class SceneObjectDebug : DisposablePlugin<SceneObjectDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->

            if (settings.interactable) {
                g.color = Color.CYAN
                SceneObjects.Interactable.getOnPlaneFlat(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (settings.wall) {
                g.color = Color.ORANGE
                SceneObjects.Wall.getOnPlaneFlat(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (settings.floor) {
                g.color = Color.GREEN
                SceneObjects.Floor.getOnPlaneFlat(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }

            if (settings.boundary) {
                g.color = Color.WHITE
                SceneObjects.Boundary.getOnPlaneFlat(Game.plane).forEach { i ->
                    val pt = i.location.center.toScreen() ?: return@forEach
                    g.drawString(i.tag.id.toString(), pt)
                    g.draw(i.location.outline())
                }
            }
        })
    }

    class Settings : PluginSettings() {
        val floor = false
        val wall = true
        val boundary = true
        val interactable = true
    }
}