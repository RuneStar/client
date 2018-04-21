package org.runestar.client.plugins.agility

import org.runestar.client.game.api.SceneObject
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.api.live.SceneObjects
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.RgbaForm
import java.awt.Color
import java.awt.RenderingHints

class Agility : DisposablePlugin<Agility.Settings>() {

    private companion object {
        val isObstaclePredicate: (SceneObject) -> Boolean = { it.id in OBSTACLE_IDS }
    }

    override val defaultSettings = Settings()

    private val obstacles: MutableSet<SceneObject> = LinkedHashSet()

    override fun start() {
        add(XScene.clear.exit.subscribe { obstacles.clear() })
        add(SceneObjects.additions.filter(isObstaclePredicate).subscribe { obstacles.add(it) })
        add(SceneObjects.removals.filter(isObstaclePredicate).subscribe { obstacles.remove(it) })
        SceneObjects.all().filterTo(obstacles, isObstaclePredicate)

        val fillColor = ctx.settings.fillColor.get()
        val outlineColor = ctx.settings.outlineColor.get()

        add(LiveCanvas.repaints.subscribe { g ->
            if (obstacles.isEmpty()) return@subscribe

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g.clip(LiveViewport.shape)

            obstacles.forEach { obstacle ->
                val loc = obstacle.location
                if (loc.plane != Game.plane || !Game.visibilityMap.isVisible(loc)) return@forEach
                val model = obstacle.primaryModel ?: return@forEach
                val shape = model.objectClickBoxOutline()

                if (ctx.settings.fill) {
                    g.color = fillColor
                    g.fill(shape)
                }

                if (ctx.settings.outline) {
                    g.color = outlineColor
                    g.draw(shape)
                }
            }
        })
    }

    override fun stop() {
        super.stop()
        obstacles.clear()
    }

    class Settings(
            val outline: Boolean = true,
            val fill: Boolean = true,
            val outlineColor: RgbaForm = RgbaForm(Color.WHITE),
            val fillColor: RgbaForm = RgbaForm(Color(255, 255, 255, 100))
    ) : PluginSettings()
}