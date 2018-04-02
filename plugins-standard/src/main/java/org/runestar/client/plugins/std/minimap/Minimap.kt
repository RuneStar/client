package org.runestar.client.plugins.std.minimap

import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XSprite
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.RgbaForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.RgbForm
import java.awt.Color

class Minimap : DisposablePlugin<Minimap.Settings>() {

    override val defaultSettings = Settings()

    private var originalDots: Array<XSprite>? = null

    override fun start() {
        super.start()

        if (settings.dots.enabled) {
            originalDots = Client.accessor.mapDotSprites.clone()
            val dots = Client.accessor.mapDotSprites
            if (settings.dots.items.enabled) {
                recolorDot(dots, 0, settings.dots.items.color.get())
            }
            if (settings.dots.npcs.enabled) {
                recolorDot(dots, 1, settings.dots.npcs.color.get())
            }
            if (settings.dots.players.enabled) {
                recolorDot(dots, 2, settings.dots.players.color.get())
            }
            if (settings.dots.friends.enabled) {
                recolorDot(dots, 3, settings.dots.friends.color.get())
            }
            if (settings.dots.team.enabled) {
                recolorDot(dots, 4, settings.dots.team.color.get())
            }
            if (settings.dots.clan.enabled) {
                recolorDot(dots, 5, settings.dots.clan.color.get())
            }
        }
    }

    override fun stop() {
        super.stop()

        originalDots?.let {
            Client.accessor.mapDotSprites = it
        }
    }

    private fun recolorDot(dots: Array<XSprite>, index: Int, color: Color) {
        val dot = dots[index].copy()
        dots[index] = dot
        val pixels = dot.pixels
        for (j in pixels.indices) {
            val c = pixels[j]
            if (c != 0 && c != 1) {
                pixels[j] = color.rgb
            }
        }
    }

    data class Settings(
            val dots: Dots = Dots()
    ) : PluginSettings() {

        data class Dots(
                val items: Dot = Dot(RgbForm(Color.RED)),
                val npcs: Dot = Dot(RgbForm(Color.ORANGE)),
                val players: Dot = Dot(RgbForm(Color.WHITE)),
                val friends: Dot = Dot(RgbForm(Color.GREEN)),
                val team: Dot = Dot(RgbForm(Color.BLUE)),
                val clan: Dot = Dot(RgbForm(Color.MAGENTA)),
                val enabled: Boolean = false
        )

        data class Dot(
                val color: RgbForm,
                val enabled: Boolean = false
        )
    }
}