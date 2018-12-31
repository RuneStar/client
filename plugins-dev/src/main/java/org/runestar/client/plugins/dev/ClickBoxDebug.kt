package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Model
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.SceneElementKind
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.api.live.SceneElements
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class ClickBoxDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs: MutableSet<SceneElement> = LinkedHashSet()

    override fun onStart() {
        add(SceneElements.clears.subscribe { objs.clear() })
        add(SceneElements.removals.subscribe { objs.remove(it) })
        add(SceneElements.additions.filter(SceneElement::isInteractable).subscribe { objs.add(it) })
        SceneElements.all().filterTo(objs, SceneElement::isInteractable)

        add(LiveCanvas.repaints.subscribe { g ->
            objs.forEach {
                g.color = colorFor(it)
                if (it.isObject) {
                    it.models.forEach { drawObject(g, it) }
                } else {
                    it.models.forEach { drawOther(g, it) }
                }
            }
        })
    }

    override fun onStop() {
        objs.clear()
    }

    private fun colorFor(obj: SceneElement): Color {
        return when (obj.kind) {
            SceneElementKind.GROUND_ITEM -> Color.RED
            SceneElementKind.NPC -> Color.YELLOW
            SceneElementKind.PLAYER -> Color.WHITE
            SceneElementKind.OBJECT -> when (obj) {
                is SceneElement.Boundary -> Color.MAGENTA
                is SceneElement.Game -> Color.BLUE
                is SceneElement.Floor -> Color.CYAN
                is SceneElement.Wall -> Color.ORANGE
                else -> throw IllegalStateException()
            }
        }
    }

    private fun shouldDraw(model: Model): Boolean {
        val pos = model.base
        val tile = pos.sceneTile
        if (tile.plane != Game.plane) return false
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