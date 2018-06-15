package org.runestar.client.plugins.emojis

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Sprite
import org.runestar.client.game.api.TextSymbol
import org.runestar.client.game.api.live.Chat
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.raw.Client
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

    private var shortCodes: List<String> = emptyList()

    private var spritesStartIndex = -1

    override fun start() {
        val ids = readEmojiIds()
        shortCodes = ids.map { it.shortCode }
        if (spritesStartIndex == -1) {
            expandModIcons()
        }
        addSprites(ids)

        add(Chat.messageAdditions.startWith(Chat).subscribe { msg ->
            msg.text = replaceEmojis(msg.text)
        })
        add(Game.ticks.subscribe {
            Players.forEach { p ->
                val text = p.overheadText ?: return@forEach
                p.overheadText = replaceEmojis(text)
            }
        })
    }

    override fun stop() {
        super.stop()
        Client.accessor.abstractFont_modIconSprites.fill(
                null,
                spritesStartIndex,
                spritesStartIndex + shortCodes.size
        )
        shortCodes = emptyList()
    }

    private fun readEmojiIds(): List<EmojiId> {
        javaClass.getResource(NAMES_CSV_NAME).openStream().bufferedReader().useLines {
            return it.map { it.split(',').let { EmojiId(it[0], it[1].toInt(), it[2].toInt()) } }.toList()
        }
    }

    private fun readSpriteSheet(): BufferedImage {
        return ImageIO.read(javaClass.getResource(SPRITE_SHEET_NAME))
    }

    private fun addSprites(ids: List<EmojiId>) {
        val sheet = readSpriteSheet()
        ids.forEachIndexed { i, id ->
            val subImage = sheet.getSubimage(
                    id.x * (SPRITE_SIZE + 2) + 1,
                    id.y * (SPRITE_SIZE + 2) + 1,
                    SPRITE_SIZE, SPRITE_SIZE
            )
            val sprite = Sprite.Indexed.copy(subImage).accessor
            sprite.height = sprite.height - 4 // tricks the game into drawing the sprite further down
            Client.accessor.abstractFont_modIconSprites[i + spritesStartIndex] = sprite
        }
    }

    private fun expandModIcons() {
        val originalArray = Client.accessor.abstractFont_modIconSprites
        spritesStartIndex = originalArray.size
        Client.accessor.abstractFont_modIconSprites = originalArray.copyOf(originalArray.size + shortCodes.size)
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
                val shortCodeIndex = shortCodes.indexOf(potentialShortCode)
                i = colon2
                if (shortCodeIndex == -1) {
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