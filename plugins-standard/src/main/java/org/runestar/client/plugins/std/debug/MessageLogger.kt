package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class MessageLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(Chat.messages.subscribe { m ->
            logger.info("type=${MessageType.LOOKUP[m.type]}, $m")
        })
    }
}