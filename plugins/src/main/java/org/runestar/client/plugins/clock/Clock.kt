package org.runestar.client.plugins.clock

import com.google.common.base.Stopwatch
import org.runestar.client.api.forms.DateTimeFormatterForm
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.ComponentId
import org.runestar.client.api.game.live.Components
import org.runestar.client.api.game.live.Worlds
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings
import java.time.Duration
import java.time.Instant

class Clock : DisposablePlugin<Clock.Settings>() {

    private companion object {
        const val DEFAULT_TEXT = "Report"
    }

    override val defaultSettings = Settings()

    private val loginTimer: Stopwatch = Stopwatch.createUnstarted()

    override fun onStart() {
        add(XClient.drawLoggedIn.enter.subscribe {
            Components[ComponentId.CHAT_REPORT_TEXT]?.text = if (settings.loginTime) {
                loginElapsedTime() ?: DEFAULT_TEXT
            } else {
                settings.dateTimeFormatter.value.format(Instant.now())
            }
        })
        if (settings.loginTime) {
            add(Worlds.enter.subscribe { loginTimer.reset().start() })
        }
    }

    override fun onStop() {
        loginTimer.reset()
        Components[ComponentId.CHAT_REPORT_TEXT]?.text = DEFAULT_TEXT
    }

    private fun loginElapsedTime(): String? {
        val duration = loginTimer.elapsed()
        if (duration == Duration.ZERO) return null
        val hrs = duration.toHours()
        val mins = duration.toMinutesPart()
        val seconds = duration.toSecondsPart()
        return "%d:%02d:%02d".format(hrs, mins, seconds)
    }

    class Settings(
            val loginTime: Boolean = true,
            val dateTimeFormatter: DateTimeFormatterForm = DateTimeFormatterForm("hh:mm:ss", null)
    ) : PluginSettings()
}