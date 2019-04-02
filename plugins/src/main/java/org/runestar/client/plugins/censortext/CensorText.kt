package org.runestar.client.plugins.censortext

import io.reactivex.Observable
import org.runestar.client.api.forms.RegexForm
import org.runestar.client.api.util.CyclicCache
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.access.XAbstractFont
import org.runestar.client.plugins.spi.PluginSettings

class CensorText : DisposablePlugin<CensorText.Settings>() {

    override val name = "Censor Text"

    override val defaultSettings = Settings()

    private val cache = CyclicCache<String, String>()

    override fun onStart() {
        val drawings = Observable.mergeArray(
                XAbstractFont.lineWidth.enter,
                XAbstractFont.draw.enter,
                XAbstractFont.drawAlpha.enter,
                XAbstractFont.drawCentered.enter,
                XAbstractFont.drawRightAligned.enter,
                XAbstractFont.drawLines.enter,
                XAbstractFont.drawCenteredShake.enter,
                XAbstractFont.drawCenteredWave.enter,
                XAbstractFont.drawCenteredWave2.enter,
                XAbstractFont.drawRandomAlphaAndSpacing.enter
        )
        add(drawings.subscribe { it.arguments[0] = cache.get(it.arguments[0] as String) { censor(it) } })
        add(Game.ticks.subscribe { cache.cycle() })
    }

    override fun onStop() {
        cache.clear()
    }

    private fun censor(text: String): String {
        return settings.replacements.fold(text) { s, (match, replacement) ->
            s.replace(match.value, replacement)
        }
    }

    class Settings(
            val replacements: List<StringReplace> = listOf(
                    StringReplace(RegexForm("bank", setOf(RegexOption.IGNORE_CASE)), "***")
            )
    ) : PluginSettings()

    data class StringReplace(
            val match: RegexForm,
            val replacement: String
    )
}