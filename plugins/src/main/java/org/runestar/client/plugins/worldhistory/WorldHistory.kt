package org.runestar.client.plugins.worldhistory

import com.google.common.collect.EvictingQueue
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings
import java.util.*

class WorldHistory : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "World History"

    private val history: Queue<Int> = EvictingQueue.create(14)

    override fun start() {
        add(Worlds.enter.subscribe {
            history.add(it.id - 300)
            val msg = "World History: ${history.reversed().joinToString()}"
            CLIENT.addMessage(0, "", msg, null)
        })
    }

    override fun stop() {
        super.stop()
        history.clear()
    }
}