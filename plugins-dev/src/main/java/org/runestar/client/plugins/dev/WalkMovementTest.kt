package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Movement
import org.runestar.client.game.api.OctantDirection
import org.runestar.client.game.api.live.Canvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Viewport
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.BasicStroke
import java.awt.Color

class WalkMovementTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        val stroke = BasicStroke(2.0f)
        add(Canvas.repaints.subscribe { g ->
            val mousePt = Mouse.location
            val mousePos = Viewport.toGame(mousePt) ?: return@subscribe
            g.stroke = stroke
            val mouseTile = mousePos.sceneTile
            for (dir in OctantDirection.VALUES) {
                val destTile = mouseTile + dir
                if (!destTile.isLoaded) continue
                val destOutline = destTile.outline()
                g.color = if (Movement.canMove(mouseTile, dir)) {
                    Color.GREEN
                } else {
                    Color.RED
                }
                g.draw(destOutline)
            }
        })
    }
}