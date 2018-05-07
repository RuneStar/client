package org.runestar.client.plugins.dev

import org.runestar.client.game.api.SceneObject
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.api.live.SceneObjects
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color

class ObjectClickBoxDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs: MutableSet<SceneObject> = LinkedHashSet()

    override fun start() {
        add(XScene.clear.exit.subscribe { objs.clear() })
        add(SceneObjects.removals.filter(SceneObject::isInteractable).subscribe { objs.remove(it) })
        add(SceneObjects.additions.filter(SceneObject::isInteractable).subscribe { objs.add(it) })

        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.RED

            val viewport = LiveViewport.shape
            objs.forEach {
                val loc = it.location
                if (loc.plane != Game.plane || !Game.visibilityMap.isVisible(loc)) return@forEach
                val pt = loc.center.toScreen() ?: return@forEach
                if (pt !in viewport) return@forEach
                it.models.forEach {
                    g.draw(it.objectClickBoxOutline())
                }
            }
        })
    }
}