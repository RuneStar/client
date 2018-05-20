package org.runestar.client.plugins.hidelowerfloors

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin

class HideLowerFloors : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Hide Lower Floors"

    override fun start() {
        add(Game.ticks.subscribe { Client.accessor.scene.minPlane = Game.plane })
    }

    override fun stop() {
        super.stop()
        Client.accessor.scene.minPlane = 0
    }
}