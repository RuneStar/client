package com.runesuite.client.plugins

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.FileReadWriter
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Keyboard
import com.runesuite.client.game.live.Mouse
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

class SwingEventsLogger : DisposablePlugin<SwingEventsLogger.Settings>(), FileReadWriter.Yaml<SwingEventsLogger.Settings> {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val s = settings
        if (s.focus) {
            add(Canvas.Live.focusEvents.subscribe {
                logger.debug { it }
            })
        }
        if (s.component) {
            add(Canvas.Live.componentEvents.subscribe {
                logger.debug { it }
            })
        }
        if (s.mouse.others || s.mouse.motion || s.mouse.wheel) {
            add(Mouse.events.subscribe {
                when (it.id) {
                    MouseEvent.MOUSE_WHEEL -> if (s.mouse.wheel) logger.debug { it }
                    MouseEvent.MOUSE_MOVED -> if (s.mouse.motion) logger.debug { it }
                    else -> if (s.mouse.others) logger.debug { it }
                }
            })
        }
        if (s.keyboard.all || s.keyboard.typed) {
            add(Keyboard.events.subscribe {
                if (s.keyboard.typed && it.id == KeyEvent.KEY_TYPED) logger.debug { it }
                else if (s.keyboard.all) logger.debug { it }
            })
        }
    }

    class Settings : Plugin.Settings() {
        val focus = false
        val component = false
        val mouse = Mouse()
        val keyboard = Keyboard()

        class Mouse {
            val motion = false
            val wheel = false
            val others = false
        }

        class Keyboard {
            val all = false
            val typed = false
        }
    }
}