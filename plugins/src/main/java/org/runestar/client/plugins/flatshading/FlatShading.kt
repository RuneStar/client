package org.runestar.client.plugins.flatshading

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Scene
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XUnlitModel
import org.runestar.client.api.plugins.PluginSettings

class FlatShading : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Flat Shading"

    override fun onStart() {
        add(XUnlitModel.light.exit.subscribe {
            val model = it.returned
            model.faceColors3.fill(-1)
        })
        reloadModels()
    }

    override fun onStop() {
        reloadModels()
    }

    private fun reloadModels() {
        CLIENT.objType_cachedModels.clear()
        CLIENT.npcType_cachedModels.clear()
        CLIENT.spotType_cachedModels.clear()
        CLIENT.component_cachedModels.clear()
        CLIENT.playerAppearance_cachedModels.clear()
        CLIENT.locType_cachedModels.clear()
        Scene.reload()
    }
}