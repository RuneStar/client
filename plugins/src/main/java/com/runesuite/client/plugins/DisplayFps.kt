package com.runesuite.client.plugins

import com.hunterwb.kxtra.swing.TextLayout
import com.hunterwb.kxtra.swing.drawTextLayout
import com.runesuite.client.base.Client
import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import java.awt.Color
import java.awt.Font
import java.awt.geom.Point2D

class DisplayFps : DisposablePlugin<DisplayFps.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        Client.accessor.displayFps = settings.clientDisplayFps

        if (settings.customDisplayFps) {
            val font = Font.decode("${settings.font.name}-${settings.font.style}-${settings.font.size}")
            val color = Color(settings.colorRgb)

            add(Canvas.Live.repaints.subscribe { g ->
                g.color = color
                g.font = font

                val canvas = Canvas.Live.shape
                val string = Client.accessor.fps.toString()
                val textLayout = TextLayout(string, g)
                val stringBounds = textLayout.bounds
                val drawPt = Point2D.Double(
                        canvas.getWidth() - stringBounds.width - settings.offset.x,
                        stringBounds.height + settings.offset.y)
                g.drawTextLayout(textLayout, drawPt)
            })
        }
    }

    class Settings : Plugin.Settings() {

        val font = FontDecode()
        val colorRgb = Color.YELLOW.rgb
        val clientDisplayFps = false
        val customDisplayFps = true
        val offset = Point2D.Double(2.0, 2.0)

        class FontDecode {
            val name = Font.DIALOG
            val style = "plain"
            val size = 14
        }
    }
}