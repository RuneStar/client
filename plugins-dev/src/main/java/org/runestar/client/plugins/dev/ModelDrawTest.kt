package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Model
import org.runestar.client.game.api.SceneObject
import org.runestar.client.game.api.live.*
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class ModelDrawTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs: MutableSet<SceneObject> = LinkedHashSet()

    override fun start() {
        objs.addAll(SceneObjects.all())
        add(XScene.clear.exit.subscribe { objs.clear() })
        add(SceneObjects.removals.subscribe { objs.remove(it) })
        add(SceneObjects.additions.subscribe { objs.add(it) })

        add(LiveCanvas.repaints.subscribe { g ->

            g.color = Color.CYAN
            objs.forEach {
                it.models.forEach { drawModel(g, it) }
            }

            g.color = Color.WHITE
            Players.forEach {
                it.model?.let { drawModel(g, it) }
            }

            g.color = Color.YELLOW
            Npcs.forEach {
                it.model?.let { drawModel(g, it) }
            }

            g.color = Color.GREEN
            Projectiles.forEach {
                it.model?.let { drawModel(g, it) }
            }

            // todo: ground items
        })
    }

    private fun drawModel(g: Graphics2D, model: Model) {
        val pos = model.base
        val tile = pos.sceneTile
        if (!tile.isLoaded || !Game.visibilityMap.isVisible(tile)) return
        val pt = pos.toScreen() ?: return
        if (pt !in LiveViewport.shape) return
        model.drawWireFrame(g.color)
    }
}