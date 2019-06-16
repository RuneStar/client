package org.runestar.client.plugins.flatshading

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XUnlitModel
import org.runestar.client.plugins.spi.PluginSettings

class FlatShading : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Flat Shading"

    override fun onStart() {
        add(XUnlitModel.light.exit.subscribe {
            val model = it.returned
            model.faceColors3.fill(-1)
        })
        clearModelCache()
    }

    override fun onStop() {
        clearModelCache()
    }

    private fun clearModelCache() {
        CLIENT.objType_cachedModels.clear()
        CLIENT.npcType_cachedModels.clear()
        CLIENT.spotType_cachedModels.clear()
        CLIENT.component_cachedModels.clear()
        CLIENT.playerAppearance_cachedModels.clear()
        CLIENT.locType_cachedModels.clear()
    }
}