package org.runestar.client.plugins.dev

import com.google.common.collect.Iterables
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color

class BoundingBoxTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->

            g.color = Color.WHITE
            val actors = Iterables.concat(Players, Npcs)
            actors.forEach {
                val m = it.model ?: return@forEach
                m.drawBoundingBox(g)
            }

            g.color = Color.ORANGE
            GroundItems.onPlane(Game.plane).forEach {
                val m = it.model ?: return@forEach
                m.drawBoundingBox(g)
            }

            g.color = Color.YELLOW
            Projectiles.forEach {
                val m = it.model ?: return@forEach
                m.drawBoundingBox(g)
            }

            g.color = Color.CYAN
            SceneObjects.Game.onPlane(Game.plane).forEach {
                val m = it.model ?: return@forEach
                m.drawBoundingBox(g)
            }

            g.color = Color.RED
            SceneObjects.Floor.onPlane(Game.plane).forEach {
                val m = it.model ?: return@forEach
                m.drawBoundingBox(g)
            }

            g.color = Color.BLUE
            SceneObjects.Wall.onPlane(Game.plane).forEach {
                it.models.forEach {
                    it.drawBoundingBox(g)
                }
            }

            g.color = Color.GREEN
            SceneObjects.Boundary.onPlane(Game.plane).forEach {
                it.models.forEach {
                    it.drawBoundingBox(g)
                }
            }
        })
    }
}