package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.LiveCamera
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveScene
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
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
            val playerPt = playerPosition.toScreen() ?: return@subscribe
            g.fill(shapeAt(playerPt))

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

    class Settings : PluginSettings() {
        val font = FontForm(Font.SANS_SERIF, FontForm.BOLD, 15f)
        val color = ColorForm()
    }
}