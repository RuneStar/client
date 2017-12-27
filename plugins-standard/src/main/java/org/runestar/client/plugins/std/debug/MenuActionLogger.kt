package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class MenuActionLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(Menu.actions.subscribe { a ->
            logger.info(a.toString())
        })
    }
}