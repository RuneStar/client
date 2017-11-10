package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.LiveCamera
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.LiveScene
import com.runesuite.client.game.api.live.Players
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.util.ColorForm
import com.runesuite.client.plugins.util.FontForm
import java.awt.Font
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class CameraDebug : DisposablePlugin<CameraDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = settings.font.get()
            g.color = settings.color.get()

            val player = Players.local ?: return@subscribe
            val playerPosition = player.position.takeIf { it.isLoaded } ?: return@subscribe

            g.fill(shapeAt(playerPosition.toScreen()))

            val camAbsoluteHeight = LiveCamera.position.height - LiveScene.getTileHeight(LiveCamera.position)
            val fakeCamPosition = playerPosition.copy(height = camAbsoluteHeight + LiveScene.getTileHeight(playerPosition))
            g.fill(shapeAt(fakeCamPosition.toScreen()))

            g.drawString(LiveCamera.toString(), 5, 40)
        })
    }

    private fun shapeAt(point: Point): Shape {
        val circle = Ellipse2D.Double()
        circle.setFrameFromCenter(point, Point(point.x + 5, point.y + 5))
        return circle
    }

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}