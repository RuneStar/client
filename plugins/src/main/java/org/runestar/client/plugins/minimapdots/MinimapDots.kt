package org.runestar.client.plugins.minimapdots

import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XSprite
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class MinimapDots : DisposablePlugin<MinimapDots.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Minimap Dots"

    private var originalDots: Array<XSprite>? = null

    override fun onStart() {
        originalDots = CLIENT.mapDotSprites.clone()
        val dots = CLIENT.mapDotSprites
        if (settings.items.enabled) {
            recolorDot(dots, 0, settings.items.color.value)
        }
        if (settings.npcs.enabled) {
            recolorDot(dots, 1, settings.npcs.color.value)
        }
        if (settings.players.enabled) {
            recolorDot(dots, 2, settings.players.color.value)
        }
        if (settings.friends.enabled) {
            recolorDot(dots, 3, settings.friends.color.value)
        }
        if (settings.team.enabled) {
            recolorDot(dots, 4, settings.team.color.value)
        }
        if (settings.clan.enabled) {
            recolorDot(dots, 5, settings.clan.color.value)
        }
    }

    override fun onStop() {
        CLIENT.mapDotSprites = originalDots
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
            val items: Dot = Dot(RgbForm(Color.RED)),
            val npcs: Dot = Dot(RgbForm(Color.ORANGE)),
            val players: Dot = Dot(RgbForm(Color.WHITE)),
            val friends: Dot = Dot(RgbForm(Color.GREEN)),
            val team: Dot = Dot(RgbForm(Color.BLUE)),
            val clan: Dot = Dot(RgbForm(Color.MAGENTA))
    ) : PluginSettings() {

        data class Dot(
                val color: RgbForm,
                val enabled: Boolean = false
        )
    }
}