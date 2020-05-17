package org.runestar.client.plugins.dev

import org.runestar.client.api.game.live.Game
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.api.plugins.DisposablePlugin

class GameStateLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Game.stateChanges.subscribe {
            logger.info(it.toString())
        })
    }
}