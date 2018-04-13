package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.game.api.live.Projections
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class ProjectionDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            val mousePt = Mouse.location
            val fromViewportPos = Projections.viewport.toGame(mousePt)
            if (fromViewportPos != null && fromViewportPos.isLoaded) {
                g.color = Color.RED
                g.draw(fromViewportPos.sceneTile.outline())
                val fromViewportPt = fromViewportPos.toScreen()
                if (fromViewportPt != null) {
                    g.fill(shapeAt(fromViewportPt))
                }

                val toMinimapPt = Projections.minimap.toScreen(fromViewportPos)
                g.color = Color.GREEN
                g.fill(shapeAt(toMinimapPt))
            }
            val fromMinimapPos = Projections.minimap.toGame(mousePt)
            if (fromMinimapPos.isLoaded) {
                g.color = Color.BLUE
                g.draw(fromMinimapPos.sceneTile.outline())
                val fromMinimapPt = fromMinimapPos.toScreen()
                if (fromMinimapPt != null) {
                    g.fill(shapeAt(fromMinimapPt))
                }
            }
        })
    }

    private fun shapeAt(point: Point): Shape {
        val circle = Ellipse2D.Double()
        circle.setFrameFromCenter(point, Point(point.x + 5, point.y + 5))
        return circle
    }
}