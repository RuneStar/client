package org.runestar.client.plugins.chattimestamps

import org.runestar.client.api.forms.DateTimeFormatterForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.spi.PluginSettings
import java.time.Instant

class ChatTimestamps : DisposablePlugin<ChatTimestamps.Settings>() {

    override val name = "Chat Timestamps"

    override val defaultSettings = Settings()

    override fun onStart() {
        add(Chat.messageAdditions.subscribe { msg ->
            msg.text = stamp(msg.text)
        })
    }

    private fun stamp(msg: String): String {
        return settings.dateTimeFormatter.value.format(Instant.now()) + msg
    }

    class Settings(
            val dateTimeFormatter: DateTimeFormatterForm = DateTimeFormatterForm("'['hh:mm']' ", null)
    ) : PluginSettings()
}