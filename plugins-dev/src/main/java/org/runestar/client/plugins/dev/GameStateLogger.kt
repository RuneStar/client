package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.Game
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin

class GameStateLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(Game.stateChanges.subscribe {
            logger.info(it.toString())
        })
    }
}