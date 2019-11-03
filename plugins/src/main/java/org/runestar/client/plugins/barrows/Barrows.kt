package org.runestar.client.plugins.barrows

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Region
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

class Barrows : DisposablePlugin<PluginSettings>() {

    private companion object {
        val REGION_ID_ABOVE_GROUND = Region(55, 51).id
        val DIG_AREA_COLOR = Color.WHITE
        val DIG_AREA_STROKE = BasicStroke(0.4f)
    }

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.filter { inBarrowsAboveGround() }.subscribe(::onRepaintAboveGround))
    }

    private fun inBarrowsAboveGround(): Boolean {
        return Game.state == GameState.LOGGED_IN && Scene.regionIds.contains(REGION_ID_ABOVE_GROUND)
    }

    private fun onRepaintAboveGround(g: Graphics2D) {
        g.color = DIG_AREA_COLOR
        g.stroke = DIG_AREA_STROKE
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.clip(Viewport.shape)
        BarrowsBrother.VALUES.forEach { g.draw(it.digArea.toScene().outline()) }
    }
}