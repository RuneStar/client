package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.EntityKind
import org.runestar.client.game.api.Model
import org.runestar.client.game.api.SceneObject
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class ClickBoxDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs: MutableSet<SceneObject> = LinkedHashSet()

    override fun start() {
        objs.addAll(SceneObjects.all().filter(SceneObject::isInteractable))
        add(SceneObjects.clears.subscribe { objs.clear() })
        add(SceneObjects.removals.subscribe { objs.remove(it) })
        add(SceneObjects.additions.filter(SceneObject::isInteractable).subscribe { objs.add(it) })

        add(LiveCanvas.repaints.subscribe { g ->

            objs.forEach {
                g.color = colorForSceneObject(it)
                if (it.tag.kind == EntityKind.OBJECT) {
                    it.models.forEach { drawObject(g, it) }
                } else {
                    // ground items
                    it.models.forEach { drawOther(g, it) }
                }
            }

            g.color = Color.WHITE
            Players.forEach {
                it.model?.let { drawOther(g, it) }
            }

            g.color = Color.YELLOW
            Npcs.forEach {
                it.model?.let { drawOther(g, it) }
            }
        })
    }

    private fun colorForSceneObject(obj: SceneObject): Color {
        return when (obj) {
            is SceneObject.ItemPile -> Color.RED
            is SceneObject.Boundary -> Color.MAGENTA
            is SceneObject.Game -> Color.BLUE
            is SceneObject.Floor -> Color.CYAN
            is SceneObject.Wall -> Color.ORANGE
            else -> throw IllegalStateException()
        }
    }

    private fun shouldDraw(model: Model): Boolean {
        val pos = model.base
        val tile = pos.sceneTile
        if (!tile.isLoaded || !Game.visibilityMap.isVisible(tile)) return false
        val pt = pos.toScreen() ?: return false
        if (pt !in LiveViewport.shape) return false
        return true
    }

    private fun drawObject(g: Graphics2D, model: Model) {
        if (shouldDraw(model)) g.draw(model.objectClickBox())
    }

    private fun drawOther(g: Graphics2D, model: Model) {
        if (shouldDraw(model)) g.draw(model.boundingBox())
    }
}