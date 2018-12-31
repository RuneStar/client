package org.runestar.client.plugins.chathighlight

import org.runestar.client.api.Application
import org.runestar.client.api.forms.RegexForm
import org.runestar.client.api.forms.RgbForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.Message
import org.runestar.client.game.api.TextEffect
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.TrayIcon

class ChatHighlight : DisposablePlugin<ChatHighlight.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Chat Highlight"

    override fun onStart() {
        add(Chat.messageAdditions.subscribe { highlight(it) })
    }

    private fun highlight(msg: Message) {
        var txt = msg.text
        settings.highlights.forEach { highlight ->
            txt = txt.replace(highlight.regex.get()) { matchResult ->
                if (highlight.trayNotify) {
                    Application.trayIcon.displayMessage(
                            msg.sender,
                            msg.text,
                            TrayIcon.MessageType.NONE
                    )
                }
                settings.effect.apply(matchResult.value)
            }
        }
        msg.text = txt
    }

    class Settings(
            val underlineColor: RgbForm = RgbForm(Color.MAGENTA),
            val highlights: List<Highlight> = listOf(
                    Highlight(
                            RegexForm("lol", setOf(RegexOption.IGNORE_CASE)),
                            false
                    )
            )
    ) : PluginSettings() {

        @Transient
        val effect = TextEffect(TextEffect.Type.UNDERLINE, underlineColor.get())
    }

    data class Highlight(
            val regex: RegexForm,
            val trayNotify: Boolean
    )
}