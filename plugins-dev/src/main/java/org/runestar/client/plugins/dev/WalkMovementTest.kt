package org.runestar.client.plugins.dev

import org.runestar.client.game.api.Movement
import org.runestar.client.game.api.OctantDirection
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Projections
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import java.awt.BasicStroke
import java.awt.Color

class WalkMovementTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        val stroke = BasicStroke(2.0f)
        add(LiveCanvas.repaints.subscribe { g ->
            val mousePt = Mouse.location
            val mousePos = Projections.viewport.toGame(mousePt) ?: return@subscribe
            g.stroke = stroke
            val mouseTile = mousePos.sceneTile
            val mvmt = Movement.Walking(LiveScene)
            for (dir in OctantDirection.VALUES) {
                val destTile = mouseTile + dir
                if (!destTile.isLoaded) continue
                val destOutline = destTile.outline()
                g.color = if (mvmt.canMove(mouseTile, dir)) {
                    Color.GREEN
                } else {
                    Color.RED
                }
                g.draw(destOutline)
            }
        })
    }
}