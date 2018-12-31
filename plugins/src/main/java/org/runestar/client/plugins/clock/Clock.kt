package org.runestar.client.plugins.clock

import com.google.common.base.Stopwatch
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetId
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.Clock as Clock8

class Clock : DisposablePlugin<Clock.Settings>() {

    private companion object {
        const val DEFAULT_TEXT = "Report"

        val UTC_CLOCK: Clock8 = Clock8.systemUTC()
        val LOCAL_CLOCK: Clock8 = Clock8.systemDefaultZone()
        val JAGEX_CLOCK: Clock8 = Clock8.system(ZoneId.of("Europe/London"))

        val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)

        fun timeNow(clock: Clock8): String = LocalTime.now(clock).format(TIME_FORMATTER)
    }

    override val defaultSettings = Settings()

    private val loginTimer: Stopwatch = Stopwatch.createUnstarted()

    override fun onStart() {
        add(XClient.drawWidgetsLoggedIn.enter.subscribe {
            val w = getReportWidget() ?: return@subscribe
            w.text = when (settings.style) {
                Style.UTC -> timeNow(UTC_CLOCK)
                Style.LOCAL -> timeNow(LOCAL_CLOCK)
                Style.JAGEX -> timeNow(JAGEX_CLOCK)
                Style.LOGIN -> loginElapsedTime() ?: DEFAULT_TEXT
            }
        })
        if (settings.style == Style.LOGIN) {
            add(Worlds.enter.subscribe { loginTimer.reset().start() })
        }
    }

    override fun onStop() {
        loginTimer.reset()
        val w = getReportWidget() ?: return
        w.text = DEFAULT_TEXT
    }

    private fun getReportWidget(): Widget.Text? = Widgets[WidgetId.CHAT_REPORT_TEXT] as Widget.Text?

    private fun loginElapsedTime(): String? {
        val duration = loginTimer.elapsed()
        if (duration == Duration.ZERO) return null
        val hrs = duration.toHours()
        val mins = duration.toMinutes() % 60
        val seconds = duration.seconds % 60
        return "%d:%02d:%02d".format(hrs, mins, seconds)
    }

    class Settings(
            val style: Style = Style.LOGIN
    ) : PluginSettings()

    enum class Style {
        UTC, LOCAL, JAGEX, LOGIN;
    }
}