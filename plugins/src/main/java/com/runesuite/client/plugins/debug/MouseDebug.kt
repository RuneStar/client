package com.runesuite.client.plugins.debug

import com.google.common.cache.CacheBuilder
import com.hunterwb.kxtra.swing.drawPoint
import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Mouse
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

        val pathColor = Color(settings.path.colorRgb)
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
            val colorRgb = Color.LIGHT_GRAY.rgb
            val width = 2
            val expireMs = 2000
        }
    }
}