package org.runestar.client.plugins.std

import org.runestar.client.api.Application
import org.runestar.client.common.AwtTaskbar
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.DurationForm
import java.awt.Toolkit
import java.awt.TrayIcon
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class AfkNotifier : DisposablePlugin<AfkNotifier.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        val timeOutMillis = settings.timeOut
                .map { it.get() }
                .reduce { a, b -> a.plus(b) }
                .toMillis()

        val inputEvents = Mouse.events
                .map { Unit }
                .mergeWith(Keyboard.events.map { Unit })

        add(inputEvents
                .filter { Game.state == GameState.LOGGED_IN }
                .debounce(timeOutMillis, TimeUnit.MILLISECONDS)
                .filter { Game.state != GameState.TITLE }
                .subscribe { afkNotify() }
        )
    }

    fun afkNotify() {
        if (settings.beep) {
            Toolkit.getDefaultToolkit().beep()
        }
        if (settings.requestUserAttention) {
            AwtTaskbar.requestWindowUserAttention(Application.frame)
        }
        if (settings.trayNotify) {
            Application.trayIcon.displayMessage(
                    javaClass.simpleName,
                    "AFK Warning",
                    TrayIcon.MessageType.WARNING
            )
        }
        if (settings.requestFocus) {
            Application.frame.requestFocus()
        }
    }

    class Settings : PluginSettings() {

        val timeOut: List<DurationForm> = listOf(
                DurationForm(4, ChronoUnit.MINUTES),
                DurationForm(50, ChronoUnit.SECONDS)
        )

        val beep = true
        val requestUserAttention = true
        val requestFocus = false
        val trayNotify = true
    }
}