package org.runestar.client.plugins.chathighlight

import io.reactivex.Observable
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.RegexForm
import org.runestar.client.api.util.RgbForm
import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.TextEffect
import org.runestar.client.game.api.TextSymbol
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XAbstractFont
import org.runestar.client.game.raw.access.XChatChannel
import org.runestar.client.game.raw.access.XMessage
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
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
        add(messageAdditions.filter { it.type in MSG_TYPES }.subscribe { it.text = highlight(it.text) })
    }

    private val messageAdditions: Observable<XMessage> get() = XChatChannel.addMessage.exit.map { it.returned }

    private val decodedTags: Observable<String> get() = XAbstractFont.decodeTag.exit.map { it.arguments[0] as String }

    private fun highlight(s: String): String {
        var str = s
        ctx.settings.highlights.forEach { highlight ->
            str = str.replace(highlight.regex.get()) { matchResult ->
                ResetSymbol.tag + highlight.get().wrap(matchResult.value)
            }
        }
        return str
    }

    class Settings(
            val highlights: List<Highlight> = listOf(
                    Highlight(
                            RegexForm("lol", setOf(RegexOption.IGNORE_CASE)),
                            mapOf(
                                    TextEffect.Type.UNDERLINE to RgbForm(Color.MAGENTA),
                                    TextEffect.Type.COLOR to RgbForm(Color.MAGENTA)
                            )
                    )
            )
    ) : PluginSettings()

    data class Highlight(
            val regex: RegexForm,
            val effects: Map<TextEffect.Type, RgbForm>
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
            Client.accessor.abstractFont_previousColor = Client.accessor.abstractFont_color
            Client.accessor.abstractFont_previousShadow = Client.accessor.abstractFont_shadow
        }
    }
}