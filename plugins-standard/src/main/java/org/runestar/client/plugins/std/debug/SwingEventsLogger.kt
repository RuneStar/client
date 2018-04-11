package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

class SwingEventsLogger : DisposablePlugin<SwingEventsLogger.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        val s = ctx.settings
        if (s.focus) {
            add(LiveCanvas.focusEvents.subscribe {
                ctx.logger.info(it.toString())
            })
        }
        if (s.window) {
            add(Game.windowEvents.subscribe {
                ctx.logger.info(it.toString())
            })
        }
        if (s.component) {
            add(LiveCanvas.componentEvents.subscribe {
                ctx.logger.info(it.toString())
            })
        }
        if (s.container) {
            add(Game.containerEvents.subscribe {
                ctx.logger.info(it.toString())
            })
        }
        if (s.mouse.others || s.mouse.motion || s.mouse.wheel) {
            add(Mouse.events.subscribe {
                when (it.id) {
                    MouseEvent.MOUSE_WHEEL -> if (s.mouse.wheel) ctx.logger.info(it.toString())
                    MouseEvent.MOUSE_MOVED -> if (s.mouse.motion) ctx.logger.info(it.toString())
                    else -> if (s.mouse.others) ctx.logger.info(it.toString())
                }
            })
        }
        if (s.keyboard.all || s.keyboard.typed) {
            add(Keyboard.events.subscribe {
                if (s.keyboard.typed && it.id == KeyEvent.KEY_TYPED) ctx.logger.info(it.toString())
                else if (s.keyboard.all) ctx.logger.info(it.toString())
            })
        }
    }

    data class Settings(
            val focus: Boolean = false,
            val component: Boolean = false,
            val window: Boolean = false,
            val container: Boolean = false,
            val mouse: Mouse = Mouse(),
            val keyboard: Keyboard = Keyboard()
    ) : PluginSettings() {


        data class Mouse(
                val motion: Boolean = false,
                val wheel: Boolean = false,
                val others: Boolean = false
        )

        data class Keyboard(
                val all: Boolean = false,
                val typed: Boolean = false
        )
    }
}