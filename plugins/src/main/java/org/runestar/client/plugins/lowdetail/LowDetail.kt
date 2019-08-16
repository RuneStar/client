package org.runestar.client.plugins.lowdetail

import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings

class LowDetail : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Low Detail"

    override fun start() {
        setLowDetail(true)
    }

    override fun stop() {
        setLowDetail(false)
    }

    private fun setLowDetail(lowDetail: Boolean) {
        CLIENT.isLowDetail = lowDetail
        CLIENT.scene_isLowDetail = lowDetail
        if (Game.state == GameState.LOGGED_IN) CLIENT.updateGameState(GameState.LOADING)
    }
}