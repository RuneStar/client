package org.runestar.client.plugins.minimap

import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XSprite
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.RgbForm
import java.awt.Color

class Minimap : DisposablePlugin<Minimap.Settings>() {

    override val defaultSettings = Settings()

    private var originalDots: Array<XSprite>? = null

    override fun start() {
        originalDots = Client.accessor.mapDotSprites.clone()
        val dots = Client.accessor.mapDotSprites
        if (ctx.settings.items.enabled) {
            recolorDot(dots, 0, ctx.settings.items.color.get())
        }
        if (ctx.settings.npcs.enabled) {
            recolorDot(dots, 1, ctx.settings.npcs.color.get())
        }
        if (ctx.settings.players.enabled) {
            recolorDot(dots, 2, ctx.settings.players.color.get())
        }
        if (ctx.settings.friends.enabled) {
            recolorDot(dots, 3, ctx.settings.friends.color.get())
        }
        if (ctx.settings.team.enabled) {
            recolorDot(dots, 4, ctx.settings.team.color.get())
        }
        if (ctx.settings.clan.enabled) {
            recolorDot(dots, 5, ctx.settings.clan.color.get())
        }
    }

    override fun stop() {
        super.stop()

        Client.accessor.mapDotSprites = originalDots
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