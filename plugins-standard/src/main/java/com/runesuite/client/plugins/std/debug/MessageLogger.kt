package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.MessageType
import com.runesuite.client.game.api.live.Chat
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin

class MessageLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(Chat.messages.subscribe { m ->
            logger.info("type=${MessageType.LOOKUP[m.type]}, $m")
        })
    }
}