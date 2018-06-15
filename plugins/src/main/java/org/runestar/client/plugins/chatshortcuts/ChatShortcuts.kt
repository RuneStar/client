package org.runestar.client.plugins.chatshortcuts

import org.runestar.client.api.forms.KeyModifierForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Chat
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XKeyHandler
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.event.KeyEvent

class ChatShortcuts : DisposablePlugin<ChatShortcuts.Settings>() {

    override val name = "Chat Shortcuts"

    override val defaultSettings = Settings()

    override fun start() {
        val keyPressedEvents = XKeyHandler.keyPressed.enter.map { it.arguments[0] as KeyEvent }
        add(keyPressedEvents
                .filter { isShortcutPressed(it, ctx.settings.clearAll) }
                .delay { XClient.doCycle.enter }
                .subscribe { clearAll() }
        )
        add(keyPressedEvents
                .filter { isShortcutPressed(it, ctx.settings.clearWord) }
                .delay { XClient.doCycle.enter }
                .subscribe { clearWord() }
        )
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

    private fun isShortcutPressed(keyEvent: KeyEvent, shortcut: Shortcut): Boolean {
        return keyEvent.modifiersEx == shortcut.mask && keyEvent.keyCode == shortcut.keyCode
    }

    class Settings(
            val clearWord: Shortcut = Shortcut(setOf(KeyModifierForm.CONTROL), KeyEvent.VK_W),
            val clearAll: Shortcut = Shortcut(setOf(KeyModifierForm.CONTROL), KeyEvent.VK_BACK_SPACE)
    ) : PluginSettings()

    class Shortcut(
            val modifiers: Set<KeyModifierForm>,
            val keyCode: Int
    ) {
        @Transient
        val mask = modifiers.sumBy { it.mask }
    }
}