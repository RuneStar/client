package com.runesuite.client.plugins

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.FileReadWriter
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.Game

class GameStateLogger : DisposablePlugin<Plugin.Settings>(), FileReadWriter.Yaml<Plugin.Settings> {

    override val defaultSettings = Plugin.Settings()

    override fun start() {
        super.start()
        add(Game.stateChanges.subscribe {
            logger.debug { it }
        })
    }
}