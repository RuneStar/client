package org.runestar.client.plugins.chatcolors

import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Message
import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.TextEffect
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class ChatColors : DisposablePlugin<ChatColors.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Chat Colors"

    override fun start() {
        add(Chat.messageAdditions.startWith(Chat).subscribe { colorMessage(it) })
    }

    override fun stop() {
        super.stop()
        Chat.forEach { decolorMessage(it) }
    }

    private fun decolorMessage(msg: Message) {
        when (msg.type) {
            MessageType.CLAN_CHAT, MessageType.AUTO_CHAT, MessageType.PUBLIC, MessageType.PUBLIC_MOD,
            MessageType.TRADE_RECEIVED-> {
                msg.prefix?.let { msg.prefix = decolorString(it) }
                msg.sender = decolorString(msg.sender)
                msg.text = decolorString(msg.text)
            }
        }
    }

    private fun colorMessage(msg: Message) {
        when (msg.type) {
            MessageType.CLAN_CHAT -> {
                msg.prefix = colorString(checkNotNull(msg.prefix), settings.clanChatName.get())
                msg.sender = colorString(msg.sender, settings.clanChatSender.get())
                msg.text = colorString(msg.text, settings.clanChatText.get())
            }
            MessageType.AUTO_CHAT -> {
                msg.sender = colorString(msg.sender, settings.publicSender.get())
                msg.text = colorString(msg.text, settings.publicAutoText.get())
            }
            MessageType.PUBLIC, MessageType.PUBLIC_MOD -> {
                msg.sender = colorString(msg.sender, settings.publicSender.get())
                msg.text = colorString(msg.text, settings.publicText.get())
            }
            MessageType.TRADE_RECEIVED -> {
                msg.text = colorString(msg.text, settings.tradeReceived.get())
            }
        }
    }

    private fun colorString(s: String, color: Color): String {
        return TextEffect.Simple(TextEffect.Type.COLOR, color).openTag + s
    }

    private fun decolorString(s: String): String {
        if (!s.startsWith("<col=")) return s
        val textStartIndex = s.indexOf('>') + 1
        return s.substring(textStartIndex)
    }

    class Settings(
            val clanChatName: RgbForm = RgbForm(Color.CYAN),
            val clanChatSender: RgbForm = RgbForm(Color.YELLOW),
            val clanChatText: RgbForm = RgbForm(Color.GREEN),

            val publicSender: RgbForm = RgbForm(Color.YELLOW),
            val publicText: RgbForm = RgbForm(Color.WHITE),
            val publicAutoText: RgbForm = RgbForm(Color.WHITE),

            val tradeReceived: RgbForm = RgbForm(Color.MAGENTA)
    ) : PluginSettings()
}