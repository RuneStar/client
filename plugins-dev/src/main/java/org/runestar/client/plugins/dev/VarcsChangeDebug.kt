package org.runestar.client.plugins.dev

import org.kxtra.slf4j.debug
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.access.XVarcs
import org.runestar.client.plugins.spi.PluginSettings

class VarcsChangeDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(XVarcs.setInt.enter.subscribe {
            val index = it.arguments[0] as Int
            val newValue = it.arguments[1] as Int
            val oldValue = it.instance.getInt(index)
            logger.debug { "Varcs int $index: $oldValue -> $newValue" }
        })
        add(XVarcs.setString.enter.subscribe {
            val index = it.arguments[0] as Int
            val newValue = it.arguments[1] as String
            val oldValue = it.instance.getString(index)
            logger.debug { "Varcs str $index: $oldValue -> $newValue" }
        })
    }
}