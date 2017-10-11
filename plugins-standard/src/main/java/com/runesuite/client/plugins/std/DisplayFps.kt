//package com.runesuite.client.plugins
//
//import com.hunterwb.kxtra.swing.TextLayout
//import com.hunterwb.kxtra.swing.drawTextLayout
//import com.runesuite.client.base.Client
//import com.runesuite.client.dev.plugins.DisposablePlugin
//import com.runesuite.client.dev.plugins.Plugin
//import com.runesuite.client.game.live.Canvas
//import java.awt.geom.Point2D
//
//class DisplayFps : DisposablePlugin<DisplayFps.Settings>() {
//
//    override val defaultSettings = Settings()
//
//    override fun start() {
//        super.start()
//
//        Client.accessor.displayFps = settings.clientDisplayFps
//
//        if (settings.customDisplayFps) {
//            val font = settings.font.get()
//            val color = settings.color.get()
//            add(Canvas.Live.repaints.subscribe { g ->
//                g.color = color
//                g.font = font
//
//                val string = Client.accessor.fps.toString()
//                val textLayout = TextLayout(string, g)
//                val stringBounds = textLayout.bounds
//                val drawPt = Point2D.Double(
//                        Client.accessor.canvas.width - stringBounds.width - settings.offset.x,
//                        stringBounds.height + settings.offset.y)
//                g.drawTextLayout(textLayout, drawPt)
//            })
//        }
//    }
//
//    class Settings : Plugin.Settings() {
//
//        val font = AwtFont()
//        val color = AwtColor(255, 255, 0)
//        val clientDisplayFps = false
//        val customDisplayFps = true
//        val offset = Point2D.Double(3.0, 28.0)
//    }
//}