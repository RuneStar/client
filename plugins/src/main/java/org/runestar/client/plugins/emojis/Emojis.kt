package org.runestar.client.plugins.emojis

import io.reactivex.Observable
import org.runestar.client.api.util.CyclicCache
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.Sprite
import org.runestar.client.api.game.appendImageTag
import org.runestar.client.api.game.live.Game
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XAbstractFont
import org.runestar.client.api.plugins.PluginSettings
import javax.imageio.ImageIO

class Emojis : DisposablePlugin<PluginSettings>() {

    private companion object {
        const val SPRITE_SHEET_NAME = "emojis.png"
        const val NAMES_CSV_NAME = "names.csv"
        const val SPRITE_SIZE = 16
    }

    override val defaultSettings = PluginSettings()

    private val shortCodes = HashMap<String, Int>()

    private val cache = CyclicCache<String, String>()

    private var spritesStartIndex = -1

    override fun onStart() {
        addNames()
        if (spritesStartIndex == -1) expandModIcons()
        addSprites()
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
        add(drawings.subscribe { it.arguments[0] = cache.get(it.arguments[0] as String) { replaceEmojis(it) } })
        add(Game.ticks.subscribe { cache.cycle() })
    }

    override fun onStop() {
        CLIENT.abstractFont_modIconSprites.fill(
                null,
                spritesStartIndex,
                spritesStartIndex + shortCodes.size
        )
        shortCodes.clear()
        cache.clear()
    }

    private fun addNames() {
        val csv = javaClass.getResource(NAMES_CSV_NAME).openStream().use { it.reader().readText() }
        csv.split(',').withIndex().associateTo(shortCodes) { it.value to it.index }
    }

    private fun addSprites() {
        val sheet = ImageIO.read(javaClass.getResource(SPRITE_SHEET_NAME))
        val count = sheet.height / SPRITE_SIZE
        for (i in 0 until count) {
            val subImage = sheet.getSubimage(
                    0,
                    i * SPRITE_SIZE,
                    SPRITE_SIZE, SPRITE_SIZE
            )
            val sprite = Sprite.Indexed.copy(subImage).accessor
            sprite.height -= 4 // tricks the game into drawing the sprite further down
            CLIENT.abstractFont_modIconSprites[i + spritesStartIndex] = sprite
        }
    }

    private fun expandModIcons() {
        val originalArray = CLIENT.abstractFont_modIconSprites
        spritesStartIndex = originalArray.size
        CLIENT.abstractFont_modIconSprites = originalArray.copyOf(originalArray.size + shortCodes.size)
    }

    private fun replaceEmojis(s: String): String {
        if (s.length < 3) return s
        var colon1 = s.indexOf(':')
        if (colon1 == -1) return s
        var colon2 = s.indexOf(':', colon1 + 1)
        if (colon2 == -1) return s
        val sb = StringBuilder(s.length)
        var next = 0
        do {
            sb.append(s.substring(next, colon1))
            var code = ""
            var id: Int? = null
            if (colon1 + 1 != colon2) {
                code = s.substring(colon1 + 1, colon2)
                id = shortCodes[code]
            }
            if (id == null) {
                sb.append(':').append(code)
                colon1 = colon2
                next = colon2
            } else {
                appendImageTag(spritesStartIndex + id, sb)
                next = colon2 + 1
                colon1 = s.indexOf(':', next)
                if (colon1 == -1) break
            }
            colon2 = s.indexOf(':', colon1 + 1)
        } while (colon2 != -1)
        if (next < s.length) sb.append(s.substring(next))
        return sb.toString()
    }
}