package org.runestar.client.plugins.clanchat

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.*
import org.runestar.client.game.api.live.Chat
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class ClanChat : DisposablePlugin<PluginSettings>() {

    private companion object {
        const val SHEET_NAME = "clanchat-ranks.png"
        const val SPRITE_SIZE = 11
    }

    override val name = "Clan Chat"

    override val defaultSettings = PluginSettings()

    private var spritesStartIndex = -1

    override fun start() {
        if (spritesStartIndex == -1) {
            expandModIcons()
            addSprites()
        }
        add(clanChatMessageAdded.subscribe(::onClanChatMessage))
    }

    private val clanChatMessageAdded get() = Chat.messageAdditions.filter { it.type == MessageType.CLAN_CHAT.id }

    private fun onClanChatMessage(msg: Message) {
        val username = msg.senderUsername ?: return
        val cc = Game.clanChat ?: return
        val clanMate = cc[username] ?: return
        val rank = clanMate.rank ?: return
        msg.prefix += ' ' + TextSymbol.Image(rank.id + spritesStartIndex).tag
    }

    private fun addSprites() {
        val sheet = readSpriteSheet()
        ClanRank.VALUES.forEach { rank ->
            val index = rank.id
            val subImage = sheet.getSubimage(
                    index * SPRITE_SIZE,
                    0,
                    SPRITE_SIZE, SPRITE_SIZE
            )
            val sprite = Sprite.Indexed.copy(subImage).accessor
            Client.accessor.abstractFont_modIconSprites[index + spritesStartIndex] = sprite
        }
    }

    private fun readSpriteSheet(): BufferedImage {
        return ImageIO.read(javaClass.getResource(SHEET_NAME))
    }

    private fun expandModIcons() {
        val originalArray = Client.accessor.abstractFont_modIconSprites
        spritesStartIndex = originalArray.size
        Client.accessor.abstractFont_modIconSprites = originalArray.copyOf(originalArray.size + ClanRank.VALUES.size)
    }
}