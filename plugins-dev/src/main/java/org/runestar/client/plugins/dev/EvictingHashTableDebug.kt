package org.runestar.client.plugins.dev

import org.kxtra.slf4j.info
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.access.XEvictingDualNodeHashTable
import org.runestar.client.plugins.spi.PluginSettings

class EvictingHashTableDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(XEvictingDualNodeHashTable.get.exit.filter { it.returned == null && it.instance.remainingCapacity == 0 }.subscribe {
            val stack = Throwable().stackTrace.asList()
                    .drop(7)
                    .dropLast(1)
                    .filter { !it.methodName.contains('$') }
                    .map { "${it.className}.${it.methodName}" }
            val elementType = it.instance.deque.sentinel.nextDual.javaClass.interfaces.firstOrNull()?.simpleName ?: "?"
            logger.info {
                "Cache miss: ${it.arguments[0]} in ${it.instance}(capacity=${it.instance.capacity}, type=$elementType) at $stack"
            }
        })
    }
}