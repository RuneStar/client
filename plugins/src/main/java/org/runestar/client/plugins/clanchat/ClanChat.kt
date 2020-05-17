package org.runestar.client.plugins.clanchat

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.ChatType
import org.runestar.client.api.game.ClanRank
import org.runestar.client.api.game.Message
import org.runestar.client.api.game.Sprite
import org.runestar.client.api.game.imageTag
import org.runestar.client.api.game.live.Chat
import org.runestar.client.api.game.live.Game
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
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

    override fun onStart() {
        if (spritesStartIndex == -1) {
            expandModIcons()
            addSprites()
        }
        add(clanChatMessageAdded.subscribe(::onClanChatMessage))
    }

    private val clanChatMessageAdded get() = Chat.messageAdditions.filter { it.type == ChatType.FRIENDSCHAT }

    private fun onClanChatMessage(msg: Message) {
        val username = msg.senderUsername ?: return
        val cc = Game.clanChat ?: return
        val clanMate = cc[username] ?: return
        val rank = clanMate.rank
        if (rank !in ClanRank.FRIEND..ClanRank.OWNER) return
        msg.prefix += ' ' + imageTag(rank + spritesStartIndex)
    }

    private fun addSprites() {
        val sheet = readSpriteSheet()
        for (rank in ClanRank.FRIEND..ClanRank.OWNER) {
            val subImage = sheet.getSubimage(
                    rank * SPRITE_SIZE,
                    0,
                    SPRITE_SIZE, SPRITE_SIZE
            )
            val sprite = Sprite.Indexed.copy(subImage).accessor
            CLIENT.abstractFont_modIconSprites[rank + spritesStartIndex] = sprite
        }
    }

    private fun readSpriteSheet(): BufferedImage {
        return ImageIO.read(javaClass.getResource(SHEET_NAME))
    }

    private fun expandModIcons() {
        val originalArray = CLIENT.abstractFont_modIconSprites
        spritesStartIndex = originalArray.size
        CLIENT.abstractFont_modIconSprites = originalArray.copyOf(originalArray.size + ClanRank.OWNER + 1)
    }
}