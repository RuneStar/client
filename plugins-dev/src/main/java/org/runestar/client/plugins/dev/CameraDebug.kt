package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.LiveCamera
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class CameraDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE

            val player = Players.local ?: return@subscribe
            val playerPosition = player.position.takeIf { it.isLoaded } ?: return@subscribe
            val playerPt = playerPosition.toScreen() ?: return@subscribe
            g.fill(shapeAt(playerPt))

            g.color = Color.YELLOW
            val camAbsoluteHeight = LiveCamera.position.height - LiveScene.getTileHeight(LiveCamera.position)
            // position with x/y of player and height of camera
            val fakeCamPosition = playerPosition.copy(height = camAbsoluteHeight + LiveScene.getTileHeight(playerPosition))
            val pt = fakeCamPosition.toScreen() ?: return@subscribe
            g.fill(shapeAt(pt))

            g.drawString(LiveCamera.toString(), 5, 40)
        })
    }

    private fun shapeAt(point: Point): Shape {
        val circle = Ellipse2D.Double()
        circle.setFrameFromCenter(point, Point(point.x + 5, point.y + 5))
        return circle
    }
}