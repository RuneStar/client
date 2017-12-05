package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Game
import com.runesuite.client.game.api.live.Keyboard
import com.runesuite.client.game.api.live.LiveCanvas
import com.runesuite.client.game.api.live.Mouse
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

class SwingEventsLogger : DisposablePlugin<SwingEventsLogger.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val s = settings
        if (s.focus) {
            add(LiveCanvas.focusEvents.subscribe {
                logger.info(it.toString())
            })
        }
        if (s.window) {
            add(Game.windowEvents.subscribe {
                logger.info(it.toString())
            })
        }
        if (s.component) {
            add(LiveCanvas.componentEvents.subscribe {
                logger.info(it.toString())
            })
        }
        if (s.container) {
            add(Game.containerEvents.subscribe {
                logger.info(it.toString())
            })
        }
        if (s.mouse.others || s.mouse.motion || s.mouse.wheel) {
            add(Mouse.events.subscribe {
                when (it.id) {
                    MouseEvent.MOUSE_WHEEL -> if (s.mouse.wheel) logger.info(it.toString())
                    MouseEvent.MOUSE_MOVED -> if (s.mouse.motion) logger.info(it.toString())
                    else -> if (s.mouse.others) logger.info(it.toString())
                }
            })
        }
        if (s.keyboard.all || s.keyboard.typed) {
            add(Keyboard.events.subscribe {
                if (s.keyboard.typed && it.id == KeyEvent.KEY_TYPED) logger.info(it.toString())
                else if (s.keyboard.all) logger.info(it.toString())
            })
        }
    }

    class Settings : PluginSettings() {
        val focus = false
        val component = false
        val window = false
        val container = false
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