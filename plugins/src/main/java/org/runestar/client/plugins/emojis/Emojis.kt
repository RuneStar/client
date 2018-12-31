package org.runestar.client.plugins.emojis

import io.reactivex.Observable
import org.runestar.client.api.util.CyclicCache
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Sprite
import org.runestar.client.game.api.TextSymbol
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XAbstractFont
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class Emojis : DisposablePlugin<PluginSettings>() {

    private companion object {
        const val SPRITE_SHEET_NAME = "sheet_twitter_16.png"
        const val NAMES_CSV_NAME = "names.csv"
        const val SPRITE_SIZE = 16
        val SHORT_CODE_PATTERN = ":([^:]+?):".toPattern()
    }

    override val defaultSettings = PluginSettings()

    private var shortCodes: Map<String, Int> = emptyMap()

    private val cache = CyclicCache<String, String>()

    private var spritesStartIndex = -1

    override fun onStart() {
        val ids = readEmojiIds()
        shortCodes = ids.withIndex().associate { it.value.shortCode to it.index }
        if (spritesStartIndex == -1) {
            expandModIcons()
        }
        addSprites(ids)
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
        shortCodes = emptyMap()
        cache.clear()
    }

    private fun readEmojiIds(): List<EmojiId> {
        javaClass.getResource(NAMES_CSV_NAME).openStream().bufferedReader().useLines {
            return it.map { it.split(',').let { EmojiId(it[0], it[1].toInt(), it[2].toInt()) } }.toList()
        }
    }

    private fun readSpriteSheet(): BufferedImage = ImageIO.read(javaClass.getResource(SPRITE_SHEET_NAME))

    private fun addSprites(ids: List<EmojiId>) {
        val sheet = readSpriteSheet()
        ids.forEachIndexed { i, id ->
            val subImage = sheet.getSubimage(
                    id.x * (SPRITE_SIZE + 2) + 1,
                    id.y * (SPRITE_SIZE + 2) + 1,
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
        if (s.length < 3 || s.count { it == ':' } < 2) return s
        val matcher = SHORT_CODE_PATTERN.matcher(s)
        val sb = StringBuilder(s.length)
        var i = 0
        while (i < s.length) {
            if (matcher.find(i)) {
                val colon1 = matcher.start()
                val colon2 = matcher.end() - 1
                sb.append(s.substring(i, colon1))
                val potentialShortCode = matcher.group(1).toLowerCase()
                val shortCodeIndex = shortCodes[potentialShortCode]
                i = colon2
                if (shortCodeIndex == null) {
                    sb.append(s.substring(colon1, colon2))
                } else {
                    sb.append(TextSymbol.Image(shortCodeIndex + spritesStartIndex).tag)
                    i++
                }
            } else {
                sb.append(s.substring(i))
                break
            }
        }
        return sb.toString()
    }

    private data class EmojiId(
            val shortCode: String,
            val x: Int,
            val y: Int
    )
}