package com.runesuite.client.plugins.debug

import com.google.common.cache.CacheBuilder
import com.hunterwb.kxtra.swing.drawPoint
import com.runesuite.client.core.api.live.Canvas
import com.runesuite.client.core.api.live.Mouse
import com.runesuite.client.plugins.framework.DisposablePlugin
import com.runesuite.client.plugins.framework.Plugin
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Point
import java.awt.event.MouseEvent
import java.util.concurrent.TimeUnit

class MouseDebug : DisposablePlugin<MouseDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val pathCache = CacheBuilder.newBuilder()
                .expireAfterWrite(settings.path.expireMs.toLong(), TimeUnit.MILLISECONDS)
                .build<Point, Unit>()

        val pathColor = settings.path.color.color()
        val pathStroke = BasicStroke(settings.path.width.toFloat())

        add(Mouse.events.subscribe {
            when (it.id) {
                MouseEvent.MOUSE_MOVED -> {
                    pathCache.put(it.point, Unit)
                }
            }
        })

        add(Canvas.Live.repaints.subscribe { g ->
            g.color = pathColor
            g.stroke = pathStroke
            pathCache.asMap().keys.forEach { pt ->
                g.drawPoint(pt)
            }
        })
    }

    class Settings : Plugin.Settings() {

        val path = Path()

        class Path {
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