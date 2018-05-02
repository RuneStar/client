package org.runestar.client.plugins.motherlodemine

import com.google.common.collect.ImmutableSet
import org.runestar.cache.generated.ObjectId
import org.runestar.client.game.api.SceneObject
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.*
import org.runestar.client.game.raw.access.XScene
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class MotherlodeMine : DisposablePlugin<PluginSettings>() {

    private companion object {

        val DRAW_COLOR = Color(255, 200, 0, 70)

        val VEIN_IDS = ImmutableSet.of(
                ObjectId.ORE_VEIN_26661,
                ObjectId.ORE_VEIN_26662,
                ObjectId.ORE_VEIN_26663,
                ObjectId.ORE_VEIN_26664
        )
    }

    override val defaultSettings = PluginSettings()

    override val name = "Motherlode Mine"

    private val veins: MutableSet<SceneObject.Boundary> = LinkedHashSet()

    override fun start() {
        SceneObjects.Boundary.all().filterTo(veins, ::isVein)
        add(XScene.clear.enter.subscribe { veins.clear() })
        add(SceneObjects.Boundary.additions.filter(::isVein).subscribe { veins.add(it) })
        add(SceneObjects.Boundary.removals.filter(::isVein).subscribe { veins.remove(it) })
        add(LiveCanvas.repaints.subscribe(::onRepaint))
    }

    override fun stop() {
        super.stop()
        veins.clear()
    }

    private fun isVein(o: SceneObject.Boundary): Boolean {
        return VEIN_IDS.contains(o.id)
    }

    private fun onRepaint(g: Graphics2D) {

        g.clip(LiveViewport.shape)
        g.color = DRAW_COLOR
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val myLoc = Players.local?.location ?: return

        veins.forEach { vein ->
            val veinLoc = vein.location
            if (!Game.visibilityMap.isVisible(veinLoc)) return@forEach
            if (!areOnSameFloor(myLoc, veinLoc)) return@forEach
            val pt = veinLoc.center.toScreen() ?: return@forEach
            if (!g.clip.contains(pt)) return@forEach
            val model = vein.primaryModel ?: return@forEach
            model.fillTriangles(g)
        }
    }

    private fun areOnSameFloor(a: SceneTile, b: SceneTile): Boolean {
        return LiveScene.isLinkBelow(a) == LiveScene.isLinkBelow(b)
    }
}