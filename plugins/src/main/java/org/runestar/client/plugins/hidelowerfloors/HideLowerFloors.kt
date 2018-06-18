package org.runestar.client.plugins.hidelowerfloors

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings

class HideLowerFloors : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Hide Lower Floors"

    override fun start() {
        add(Game.ticks.subscribe { CLIENT.scene.minPlane = Game.plane })
    }

    override fun stop() {
        super.stop()
        CLIENT.scene.minPlane = 0
    }
}