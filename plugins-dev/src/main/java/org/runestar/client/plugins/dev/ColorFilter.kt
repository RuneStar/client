package org.runestar.client.plugins.dev

import org.kxtra.lang.intarray.replaceEach
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.spi.PluginSettings
import kotlin.math.min

class ColorFilter : DisposablePlugin<ColorFilter.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Color Filter"

    override fun start() {
        add(XRasterProvider.drawFull0.enter.filter { Game.state == GameState.LOGGED_IN }.subscribe {
            val rl = ctx.settings.redBrightness
            val gl = ctx.settings.greenBrightness
            val bl = ctx.settings.blueBrightness
            it.instance.pixels.replaceEach {
                val r = min(((it shr 16 and 0xFF) * rl).toInt(), 0xFF)
                val g = min(((it shr 8 and 0xFF) * gl).toInt(), 0xFF)
                val b = min(((it and 0xFF) * bl).toInt(), 0xFF)
                (r shl 16) + (g shl 8) + b
            }
        })
    }

    class Settings(
            val redBrightness: Double = 0.7,
            val greenBrightness: Double = 0.7,
            val blueBrightness: Double = 0.2
    ) : PluginSettings()
}