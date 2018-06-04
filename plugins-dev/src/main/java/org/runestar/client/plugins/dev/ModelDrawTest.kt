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

class ModelDrawTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs: MutableSet<SceneElement> = LinkedHashSet()

    override fun start() {
        add(SceneElements.clears.subscribe { objs.clear() })
        add(SceneElements.removals.subscribe { objs.remove(it) })
        add(SceneElements.additions.subscribe { objs.add(it) })
        objs.addAll(SceneElements.all())

        add(LiveCanvas.repaints.subscribe { g ->
            objs.forEach {
                g.color = colorFor(it)
                it.models.forEach { drawModel(g, it) }
            }
        })
    }

    override fun stop() {
        super.stop()
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

    private fun drawModel(g: Graphics2D, model: Model) {
        val pos = model.base
        val tile = pos.sceneTile
        if (!tile.isLoaded || !Game.visibilityMap.isVisible(tile)) return
        val pt = pos.toScreen() ?: return
        if (pt !in LiveViewport.shape) return
        model.drawWireFrame(g.color)
    }
}