package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.game.api.live.Camera
import org.runestar.client.game.api.live.Canvas
import org.runestar.client.game.api.live.Scene
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class CameraDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE

            val player = Players.local ?: return@subscribe
            val playerPosition = player.modelPosition.takeIf { it.isLoaded } ?: return@subscribe
            val playerPt = playerPosition.toScreen() ?: return@subscribe
            g.fill(shapeAt(playerPt))

            g.color = Color.YELLOW
            val camAbsoluteHeight = Camera.position.height - Scene.getTileHeight(Camera.position)
            // position with x/y of player and height of camera
            val fakeCamPosition = playerPosition.copy(height = camAbsoluteHeight + Scene.getTileHeight(playerPosition))
            val pt = fakeCamPosition.toScreen() ?: return@subscribe
            g.fill(shapeAt(pt))

            g.drawString(Camera.toString(), 5, 40)
        })
    }

    private fun shapeAt(point: Point): Shape {
        val circle = Ellipse2D.Double()
        circle.setFrameFromCenter(point, Point(point.x + 5, point.y + 5))
        return circle
    }
}