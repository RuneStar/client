package org.runestar.client.plugins.std.minimap

import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XSprite
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
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
                recolorDot(dots, 0, settings.dots.items.color)
            }
            if (settings.dots.npcs.enabled) {
                recolorDot(dots, 1, settings.dots.npcs.color)
            }
            if (settings.dots.players.enabled) {
                recolorDot(dots, 2, settings.dots.players.color)
            }
            if (settings.dots.friends.enabled) {
                recolorDot(dots, 3, settings.dots.friends.color)
            }
            if (settings.dots.team.enabled) {
                recolorDot(dots, 4, settings.dots.team.color)
            }
            if (settings.dots.clan.enabled) {
                recolorDot(dots, 5, settings.dots.clan.color)
            }
        }
    }

    override fun stop() {
        super.stop()

        originalDots?.let {
            Client.accessor.mapDotSprites = it
        }
    }

    private fun recolorDot(dots: Array<XSprite>, index: Int, color: ColorForm) {
        val dot = dots[index].copy()
        dots[index] = dot
        val pixels = dot.pixels
        for (j in pixels.indices) {
            val c = pixels[j]
            if (c != 0 && c != 1) {
                pixels[j] = color.get().rgb
            }
        }
    }

    data class Settings(
            val dots: Dots = Dots()
    ) : PluginSettings() {

        data class Dots(
                val items: Dot = Dot(ColorForm(Color.RED)),
                val npcs: Dot = Dot(ColorForm(Color.YELLOW)),
                val players: Dot = Dot(ColorForm(Color.WHITE)),
                val friends: Dot = Dot(ColorForm(Color.GREEN)),
                val team: Dot = Dot(ColorForm(Color.BLUE)),
                val clan: Dot = Dot(ColorForm(Color.MAGENTA)),
                val enabled: Boolean = false
        )

        data class Dot(
                val color: ColorForm,
                val enabled: Boolean = false
        )
    }
}