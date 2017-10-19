package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Camera
import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.Plugin
import java.awt.Color

class CameraDebug: DisposablePlugin<Plugin.Settings>() {

    override val defaultSettings = Plugin.Settings()

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            val x = 5
            val y = 40
            g.color = Color.WHITE
            g.drawString(Camera.Live.toString(), x, y)
        })
    }
}