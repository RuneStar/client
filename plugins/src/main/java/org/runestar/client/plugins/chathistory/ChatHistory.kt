package org.runestar.client.plugins.chathistory

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XIterableDualNodeQueue
import org.runestar.client.game.raw.access.XIterableNodeHashTable
import org.runestar.client.plugins.spi.PluginSettings

class ChatHistory : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Chat History"

    override fun start() {
        add(XIterableNodeHashTable.clear.enter.subscribe {
            if (it.instance == CLIENT.messages_hashTable) it.skipBody = true
        })

        add(XIterableDualNodeQueue.clear.enter.subscribe {
            if (it.instance == CLIENT.messages_queue) it.skipBody = true
        })

        lateinit var channels: Map<*, *>
        var messageCount = 0
        add(XClient.onLogin.enter.subscribe {
            channels = HashMap(CLIENT.messages_channels)
            messageCount = CLIENT.messages_count
        })
        add(XClient.onLogin.exit.subscribe {
            CLIENT.messages_channels.putAll(channels)
            CLIENT.messages_count = messageCount
        })
    }
}