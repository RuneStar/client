package org.runestar.client.plugins.dev

import org.runestar.client.game.api.MessageType
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class MessageLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(Chat.messages.subscribe { m ->
            ctx.logger.info("type=${MessageType.LOOKUP[m.type]}, $m")
        })
    }
}