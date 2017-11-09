package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.*
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color
import java.awt.Point
import java.awt.Shape
import java.awt.geom.Ellipse2D

class ProjectionDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()

        add(LiveCanvas.repaints.subscribe { g ->
            val mousePt = Mouse.location
//            val viewportPos = Projection.Viewport.LIVE.toGame(mousePt)
//            if (viewportPos.isLoaded) {
//                g.color = Color.RED
//                g.draw(viewportPos.sceneTile.outline())
//                g.drawPoint(viewportPos.toScreen())
//            }
            val minimapPos = Projections.minimap.toGame(mousePt)
            if (minimapPos.isLoaded) {
                g.color = Color.BLUE
                g.draw(minimapPos.sceneTile.outline())
                g.fill(shapeAt(minimapPos.toScreen()))
            }
        })
    }

    private fun shapeAt(point: Point): Shape {
        val circle = Ellipse2D.Double()
        circle.setFrameFromCenter(point, Point(point.x + 3, point.y + 3))
        return circle
    }
}