package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Game
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.Plugin

class GameStateLogger : DisposablePlugin<Plugin.Settings>() {

    override val defaultSettings = Plugin.Settings()

    override fun start() {
        super.start()
        add(Game.stateChanges.subscribe {
            logger.debug { it }
        })
    }
}