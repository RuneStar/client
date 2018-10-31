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

class Barrows : DisposablePlugin<Barrows.Settings>() {

    private companion object {
        val REGION_ID_ABOVE_GROUND = Region.of(55, 51).id
        val REGION_ID_UNDER_GROUND = Region.of(55, 151).id
        val DIG_AREA_COLOR = Color.WHITE
        val DIG_AREA_STROKE = BasicStroke(0.4f)
    }

    override val defaultSettings = Settings()

    override fun start() {
        add(LiveCanvas.repaints.filter { inBarrowsAboveGround() }.subscribe(::onRepaintAboveGround))
        if (settings.drawMinimap) {
            add(Game.ticks.filter { shouldDrawMinimap() }.subscribe { LiveMinimap.isDrawn = true })
        }
    }

    override fun stop() {
        super.stop()
        if (settings.drawMinimap && inBarrowsUnderground() && LiveMinimap.isDrawn) {
            LiveMinimap.isDrawn = false
        }
    }

    private fun inBarrowsUnderground(): Boolean {
        return Game.state == GameState.LOGGED_IN && LiveScene.regionIds.contains(REGION_ID_UNDER_GROUND)
    }

    private fun inBarrowsAboveGround(): Boolean {
        return Game.state == GameState.LOGGED_IN && LiveScene.regionIds.contains(REGION_ID_ABOVE_GROUND)
    }

    private fun shouldDrawMinimap(): Boolean {
        return !LiveMinimap.isDrawn && inBarrowsUnderground()
    }

    private fun onRepaintAboveGround(g: Graphics2D) {
        g.color = DIG_AREA_COLOR
        g.stroke = DIG_AREA_STROKE
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.clip(LiveViewport.shape)
        BarrowsBrother.VALUES.forEach { g.draw(it.digArea.toScene().outline()) }
    }

    class Settings(
            val drawMinimap: Boolean = true
    ) : PluginSettings()
}