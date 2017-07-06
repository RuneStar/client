package com.runesuite.client.plugins.debug

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Menu
import java.awt.BasicStroke
import java.awt.Color

class MenuDebug : DisposablePlugin<MenuDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val s = settings
        if (s.openings) {
            add(Menu.openings.subscribe {
                logger.debug { it }
            })
        }
        if (s.actions) {
            add(Menu.actions.subscribe {
                logger.debug { it }
            })
        }
        if (s.drawMenu || s.drawOptions) {
            add(Canvas.Live.repaints.subscribe { g ->
                if (Menu.isOpen && s.drawMenu) {
                    g.color = Color.ORANGE
                    g.stroke = BasicStroke(2f)
                    Menu.optionShapes.forEach {
                        g.draw(it)
                    }
                }
                if (s.drawOptions) {
                    val x = 5
                    var y = 40
                    g.color = Color.WHITE
                    Menu.options.forEach { op ->
                        g.drawString(op.toString(), x, y)
                        y += 13
                    }
                }
            })
        }
    }

    class Settings : Plugin.Settings() {
        val openings = false
        val actions = false
        val drawMenu = false
        val drawOptions = false
    }
}