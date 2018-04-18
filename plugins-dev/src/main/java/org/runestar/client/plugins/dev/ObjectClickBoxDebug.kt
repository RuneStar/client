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
import java.util.concurrent.CopyOnWriteArraySet

class ObjectClickBoxDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    private val objs = CopyOnWriteArraySet<SceneObject>()

    private val addition: (SceneObject) -> Unit = {
        if (it.tag.isInteractable) {
            objs.add(it)
        }
    }

    private val removal: (SceneObject) -> Unit = {
        if (it.tag.isInteractable) {
            objs.remove(it)
        }
    }

    override fun start() {
        add(XScene.clear.exit.subscribe { objs.clear() })

        add(SceneObjects.Wall.removals.subscribe(removal))
        add(SceneObjects.Floor.removals.subscribe(removal))
        add(SceneObjects.Boundary.removals.subscribe(removal))
        add(SceneObjects.Interactable.removals.subscribe(removal))

        add(SceneObjects.Wall.additions.subscribe(addition))
        add(SceneObjects.Floor.additions.subscribe(addition))
        add(SceneObjects.Boundary.additions.subscribe(addition))
        add(SceneObjects.Interactable.additions.subscribe(addition))

        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.WHITE
            val viewport = LiveViewport.shape
            objs.forEach {
                val loc = it.location
                if (loc.plane != Game.plane) return@forEach
                val center = loc.center.toScreen() ?: return@forEach
                if (center !in viewport) return@forEach
                it.models.forEach {
                    g.draw(it.objectClickBoxOutline())
                }
            }
        })
    }
}