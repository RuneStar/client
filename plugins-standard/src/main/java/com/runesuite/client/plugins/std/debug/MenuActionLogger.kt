package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Menu
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin

class MenuActionLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(Menu.actions.subscribe { a ->
            logger.info(a.toString())
        })
    }
}