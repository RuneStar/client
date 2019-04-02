package org.runestar.client.plugins.chatshortcuts

import org.runestar.client.api.forms.KeyStrokeForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Chat
import org.runestar.client.game.api.live.Keyboard
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.PluginSettings

class ChatShortcuts : DisposablePlugin<ChatShortcuts.Settings>() {

    override val name = "Chat Shortcuts"

    override val defaultSettings = Settings()

    override fun onStart() {
        Keyboard.strokes
                .filter(settings.clearAll.value::equals)
                .delay { XClient.doCycle.enter }
                .subscribe { clearAll() }
                .add()

        Keyboard.strokes
                .filter(settings.clearWord.value::equals)
                .delay { XClient.doCycle.enter }
                .subscribe { clearWord() }
                .add()
    }

    private fun clearAll() {
        Chat.typedText = ""
    }

    private fun clearWord() {
        val text = Chat.typedText ?: return
        Chat.typedText = if (text.endsWith(' ')) {
            text.dropLastWhile { it == ' ' }
        } else {
            text.dropLastWhile { it != ' ' }
        }
    }

    class Settings(
            val clearWord: KeyStrokeForm = KeyStrokeForm("control released W"),
            val clearAll: KeyStrokeForm = KeyStrokeForm("control released BACK_SPACE")
    ) : PluginSettings()
}