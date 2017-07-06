package com.runesuite.client.plugins

import com.runesuite.client.base.Client
import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import java.awt.Color
import java.awt.Font
import java.awt.Point
import java.awt.font.TextLayout

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

                val stringBounds = TextLayout(string, font, g.fontRenderContext).bounds.bounds

                g.drawString(string,
                        canvas.width - stringBounds.width - settings.offset.x,
                        stringBounds.height + settings.offset.y)
            })
        }
    }

    class Settings : Plugin.Settings() {

        val font = FontDecode()
        val colorRgb = Color.YELLOW.rgb
        val clientDisplayFps = false
        val customDisplayFps = true
        val offset = Point(1, 1)

        class FontDecode {
            val name = Font.DIALOG
            val style = "plain"
            val size = 12
        }
    }
}