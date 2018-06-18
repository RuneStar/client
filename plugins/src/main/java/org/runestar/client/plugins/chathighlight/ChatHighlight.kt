package org.runestar.client.plugins.chathighlight

import io.reactivex.Observable
import org.runestar.client.api.Application
import org.runestar.client.api.forms.RegexForm
import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Message
import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.TextEffect
import org.runestar.client.game.api.TextSymbol
import org.runestar.client.game.api.live.Chat
import org.runestar.client.game.raw.access.XAbstractFont
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.TrayIcon
import java.util.function.Supplier

class ChatHighlight : DisposablePlugin<ChatHighlight.Settings>() {

    private companion object {
        val MSG_TYPES = setOf(
                MessageType.PUBLIC_MOD.id,
                MessageType.PUBLIC.id,
                MessageType.PRIVATE_RECEIVED_MOD.id,
                MessageType.PRIVATE_RECEIVED.id,
                MessageType.PRIVATE_SENT.id,
                MessageType.AUTO_CHAT.id,
                MessageType.CLAN_CHAT.id
        )
    }

    override val defaultSettings = Settings()

    override val name = "Chat Highlight"

    override fun start() {
        decodedTags.filter { it == ResetSymbol.id }.subscribe { ResetSymbol.run() }
        add(Chat.messageAdditions.filter { it.type in MSG_TYPES }.subscribe { highlight(it) })
    }

    private val decodedTags: Observable<String> get() = XAbstractFont.decodeTag.exit.map { it.arguments[0] as String }

    private fun highlight(msg: Message) {
        var txt = msg.text
        ctx.settings.highlights.forEach { highlight ->
            txt = txt.replace(highlight.regex.get()) { matchResult ->
                if (highlight.trayNotify) {
                    Application.trayIcon.displayMessage(
                            msg.sender,
                            msg.text,
                            TrayIcon.MessageType.NONE
                    )
                }
                ResetSymbol.tag + highlight.get().apply(matchResult.value)
            }
        }
        msg.text = txt
    }

    class Settings(
            val highlights: List<Highlight> = listOf(
                    Highlight(
                            RegexForm("lol", setOf(RegexOption.IGNORE_CASE)),
                            mapOf(
                                    TextEffect.Type.UNDERLINE to RgbForm(Color.MAGENTA),
                                    TextEffect.Type.COLOR to RgbForm(Color.MAGENTA)
                            ),
                            false
                    )
            )
    ) : PluginSettings()

    data class Highlight(
            val regex: RegexForm,
            val effects: Map<TextEffect.Type, RgbForm>,
            val trayNotify: Boolean
    ) : Supplier<TextEffect> {

        @Transient
        private val value: TextEffect = TextEffect.Composite(
                effects.entries.map {
                    TextEffect.Simple(it.key, it.value.get())
                }
        )

        override fun get(): TextEffect = value
    }

    // todo
    // attempts to get better nested tag behavior
    private object ResetSymbol : TextSymbol, Runnable {

        const val id = "$"

        override val tag: String = "<$id>"

        override fun run() {
            CLIENT.abstractFont_previousColor = CLIENT.abstractFont_color
            CLIENT.abstractFont_previousShadow = CLIENT.abstractFont_shadow
        }
    }
}