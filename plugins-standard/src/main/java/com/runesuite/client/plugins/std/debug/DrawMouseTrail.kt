package com.runesuite.client.plugins.std.debug

import com.google.common.cache.CacheBuilder
import com.hunterwb.kxtra.swing.graphics.drawPoint
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.Mouse
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Point
import java.awt.event.MouseEvent
import java.util.concurrent.TimeUnit

class DrawMouseTrail : DisposablePlugin<DrawMouseTrail.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val pathCache = CacheBuilder.newBuilder()
                .expireAfterWrite(settings.trail.expireMs.toLong(), TimeUnit.MILLISECONDS)
                .build<Point, Unit>()

        val pathColor = settings.trail.color.color()
        val pathStroke = BasicStroke(settings.trail.width.toFloat())

        add(Mouse.events.filter { it.id == MouseEvent.MOUSE_MOVED }
                .subscribe {
                    pathCache.put(it.point, Unit)
                }
        )

        add(LiveCanvas.repaints.subscribe { g ->
            g.color = pathColor
            g.stroke = pathStroke
            pathCache.asMap().keys.forEach { pt ->
                g.drawPoint(pt)
            }
        })
    }

    class Settings : PluginSettings() {

        val trail = Trail()

        class Trail {
            val color = Color()
            val width = 2
            val expireMs = 2000

            class Color {
                val r = 255
                val g = 255
                val b = 255
                val a = 255

                fun color(): java.awt.Color {
                    return Color(r, g, b, a)
                }
            }
        }
    }
}