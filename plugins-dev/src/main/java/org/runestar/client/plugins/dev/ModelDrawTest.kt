package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Model
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.SceneElementKind
import org.runestar.client.game.api.live.Canvas
import org.runestar.client.game.api.live.Scene
import org.runestar.client.game.api.live.Viewport
import org.runestar.client.game.api.live.SceneElements
import org.runestar.client.game.api.live.VisibilityMap
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class ModelDrawTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs: MutableSet<SceneElement> = LinkedHashSet()

    override fun onStart() {
        add(SceneElements.cleared.subscribe { objs.clear() })
        add(SceneElements.removed.subscribe { objs.remove(it) })
        add(SceneElements.added.subscribe { objs.add(it) })
        Scene.reload()

        add(Canvas.repaints.subscribe { g ->
            objs.forEach {
                g.color = colorFor(it)
                it.models.forEach { drawModel(g, it) }
            }
        })
    }

    override fun onStop() {
        objs.clear()
    }

    private fun colorFor(obj: SceneElement): Color {
        return when (obj.tag.kind) {
            SceneElementKind.OBJ -> Color.RED
            SceneElementKind.NPC -> Color.YELLOW
            SceneElementKind.PLAYER -> Color.WHITE
            SceneElementKind.LOC -> when (obj) {
                is SceneElement.Wall -> Color.MAGENTA
                is SceneElement.Scenery -> Color.BLUE
                is SceneElement.FloorDecoration -> Color.CYAN
                is SceneElement.WallDecoration -> Color.ORANGE
                else -> throw IllegalStateException()
            }
            else -> error(obj)
        }
    }

    private fun drawModel(g: Graphics2D, model: Model) {
        val pos = model.base
        val tile = pos.sceneTile
        if (!tile.isLoaded || !VisibilityMap.isVisible(tile)) return
        val pt = pos.toScreen() ?: return
        if (pt !in Viewport) return
        model.drawWireFrame(g.color)
    }
}