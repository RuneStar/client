package org.runestar.client.plugins.motherlodemine

import com.google.common.collect.ImmutableSet
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.ObjectId
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class MotherlodeMine : DisposablePlugin<PluginSettings>() {

    private companion object {

        val VEIN_IDS = ImmutableSet.of(
                ObjectId.ORE_VEIN_26661,
                ObjectId.ORE_VEIN_26662,
                ObjectId.ORE_VEIN_26663,
                ObjectId.ORE_VEIN_26664
        )
    }

    override val defaultSettings = PluginSettings()

    override val name = "Motherlode Mine"

    private val veins: MutableSet<SceneElement.Boundary> = LinkedHashSet()

    override fun onStart() {
        add(SceneElements.clears.subscribe { veins.clear() })
        add(SceneElements.Boundary.additions.filter(::isVein).subscribe { veins.add(it) })
        add(SceneElements.Boundary.removals.filter(::isVein).subscribe { veins.remove(it) })
        SceneElements.Boundary.all().filterTo(veins, ::isVein)

        add(LiveCanvas.repaints.subscribe(::onRepaint))
    }

    override fun onStop() {
        veins.clear()
    }

    private fun isVein(o: SceneElement.Boundary): Boolean {
        return VEIN_IDS.contains(o.id)
    }

    private fun onRepaint(g: Graphics2D) {

        g.clip(LiveViewport.shape)
        g.color = Color.LIGHT_GRAY

        val myLoc = Players.local?.location ?: return

        veins.forEach { vein ->
            val veinLoc = vein.location
            if (!Game.visibilityMap.isVisible(veinLoc)) return@forEach
            if (!areOnSameFloor(myLoc, veinLoc)) return@forEach
            val pt = veinLoc.center.copy(height = 200).toScreen() ?: return@forEach
            if (!g.clip.contains(pt)) return@forEach
            val model = vein.model ?: return@forEach
            model.drawWireFrame(g.color)
        }
    }

    private fun areOnSameFloor(a: SceneTile, b: SceneTile): Boolean {
        return LiveScene.isLinkBelow(a) == LiveScene.isLinkBelow(b)
    }
}