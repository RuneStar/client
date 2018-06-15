package org.runestar.client.plugins.agility

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.api.live.SceneElements
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class Agility : DisposablePlugin<Agility.Settings>() {

    override val defaultSettings = Settings()

    private val obstacles: MutableSet<SceneElement> = LinkedHashSet()

    override fun start() {
        add(XScene.clear.exit.subscribe { obstacles.clear() })
        add(SceneElements.Object.additions.filter(::isObstacle).subscribe { obstacles.add(it) })
        add(SceneElements.Object.removals.filter(::isObstacle).subscribe { obstacles.remove(it) })
        SceneElements.Object.all().filterTo(obstacles, ::isObstacle)

        add(LiveCanvas.repaints.subscribe(::onRepaint))
    }

    override fun stop() {
        super.stop()
        obstacles.clear()
    }

    private fun isObstacle(o: SceneElement): Boolean {
        return o.id in OBSTACLE_IDS
    }

    private fun onRepaint(g: Graphics2D) {
        if (obstacles.isEmpty()) return

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.clip(LiveViewport.shape)

        obstacles.forEach { obstacle ->
            drawObstacle(g, obstacle)
        }
    }

    private fun drawObstacle(g: Graphics2D, obstacle: SceneElement) {
        val loc = obstacle.location
        if (loc.plane != Game.plane || !Game.visibilityMap.isVisible(loc)) return
        val model = obstacle.model ?: return
        val shape = model.objectClickBox()

        if (ctx.settings.fill) {
            g.color = ctx.settings.fillColor.get()
            g.fill(shape)
        }

        if (ctx.settings.outline) {
            g.color = ctx.settings.outlineColor.get()
            g.draw(shape)
        }
    }

    class Settings(
            val outline: Boolean = true,
            val fill: Boolean = true,
            val outlineColor: RgbaForm = RgbaForm(Color.WHITE),
            val fillColor: RgbaForm = RgbaForm(Color(255, 255, 255, 100))
    ) : PluginSettings()
}