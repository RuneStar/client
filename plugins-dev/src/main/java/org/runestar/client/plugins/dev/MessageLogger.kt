package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.spi.PluginSettings

class MessageLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(Chat.messageAdditions.subscribe { m ->
            logger.info("type=${MessageType.LOOKUP[m.type]}, $m")
        })
    }
}