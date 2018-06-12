package org.runestar.client.plugins.chatcolors

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.RgbForm
import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.TextEffect
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XChatBox
import org.runestar.client.game.raw.access.XMessage
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color

class ChatColors : DisposablePlugin<ChatColors.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Chat Colors"

    override fun start() {
        add(XChatBox.addMessage.exit.subscribe { colorMessage(it.returned) })
        existingMessagesForEach { colorMessage(it) }
    }

    override fun stop() {
        super.stop()
        existingMessagesForEach { decolorMessage(it) }
    }

    private inline fun existingMessagesForEach(action: (XMessage) -> Unit) {
        Client.accessor.chatBoxes.values.forEach { chatBox ->
            chatBox as XChatBox
            chatBox.messages.forEach { msg ->
                if (msg != null) {
                    action(msg)
                }
            }
        }
    }

    private fun decolorMessage(msg: XMessage) {
        when (msg.type) {
            MessageType.CLAN_CHAT.id, MessageType.AUTO_CHAT.id, MessageType.PUBLIC.id, MessageType.PUBLIC_MOD.id,
            MessageType.TRADE_RECEIVED.id-> {
                msg.prefix = decolorString(msg.prefix)
                msg.sender = decolorString(msg.sender)
                msg.text = decolorString(msg.text)
            }
        }
    }

    private fun colorMessage(msg: XMessage) {
        when (msg.type) {
            MessageType.CLAN_CHAT.id -> {
                msg.prefix = colorString(msg.prefix, ctx.settings.clanChatName.get())
                msg.sender = colorString(msg.sender, ctx.settings.clanChatSender.get())
                msg.text = colorString(msg.text, ctx.settings.clanChatText.get())
            }
            MessageType.AUTO_CHAT.id -> {
                msg.sender = colorString(msg.sender, ctx.settings.publicSender.get())
                msg.text = colorString(msg.text, ctx.settings.publicAutoText.get())
            }
            MessageType.PUBLIC.id, MessageType.PUBLIC_MOD.id -> {
                msg.sender = colorString(msg.sender, ctx.settings.publicSender.get())
                msg.text = colorString(msg.text, ctx.settings.publicText.get())
            }
            MessageType.TRADE_RECEIVED.id -> {
                msg.text = colorString(msg.text, ctx.settings.tradeReceived.get())
            }
        }
    }

    private fun colorString(s: String?, color: Color): String? {
        if (s == null) return null
        return TextEffect.Simple(TextEffect.Type.COLOR, color).openTag + s
    }

    private fun decolorString(s: String?): String? {
        if (s == null) return null
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