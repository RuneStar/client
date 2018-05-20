package org.runestar.client.plugins.wintertodt

import org.runestar.client.game.api.Projectile
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Projectiles
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class Wintertodt : DisposablePlugin<PluginSettings>() {

    private companion object {
        val STROKE_3 = BasicStroke(3f)
        val DRAW_COLOR = Color(0, 0, 255, 190)
    }

    override val defaultSettings = PluginSettings()

    private var fallingSnow: MutableMap<Projectile, SceneTile> = LinkedHashMap()

    override fun start() {
        add(
                Projectiles.destinationChanges
                        .filter { (proj, _) -> proj.isFallingSnow() }
                        .subscribe { (proj, pos) -> fallingSnow[proj] = pos.sceneTile }
        )
        add(LiveCanvas.repaints.subscribe(::onRepaint))
    }

    private fun Projectile.isFallingSnow(): Boolean {
        return id == 501 || id == 1310
    }

    private fun onRepaint(g: Graphics2D) {
        g.color = DRAW_COLOR
        g.stroke = STROKE_3
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val itr = fallingSnow.iterator()
        while (itr.hasNext()) {
            val (proj, tile) = itr.next()

            if (Game.visibilityMap.isVisible(tile)) {
                g.draw(tile.outline())
            }

            if (Game.cycle > proj.accessor.cycleEnd) {
                itr.remove()
            }
        }
    }
}