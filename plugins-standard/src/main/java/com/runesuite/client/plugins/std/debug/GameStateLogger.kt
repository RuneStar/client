package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Game
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin

class GameStateLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(Game.stateChanges.subscribe {
            logger.info(it.toString())
        })
    }
}