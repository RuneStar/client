package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Chat
import org.runestar.client.api.plugins.PluginSettings

class MessageLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Chat.messageAdditions.subscribe { m ->
            logger.info(m.toString())
        })
    }
}