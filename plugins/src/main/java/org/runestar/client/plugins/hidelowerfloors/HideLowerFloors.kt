package org.runestar.client.plugins.hidelowerfloors

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Game
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class HideLowerFloors : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Hide Lower Floors"

    override fun onStart() {
        add(XClient.drawLoggedIn.enter.subscribe { CLIENT.scene.minPlane = Game.plane })
    }

    override fun onStop() {
        CLIENT.scene.minPlane = 0
    }
}