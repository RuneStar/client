package org.runestar.client.plugins.flatshading

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XModelData
import org.runestar.client.plugins.spi.PluginSettings

class FlatShading : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Flat Shading"

    override fun onStart() {
        add(XModelData.toModel.exit.subscribe {
            val model = it.returned
            model.faceColors3.fill(-1)
        })
        clearModelCache()
    }

    override fun onStop() {
        clearModelCache()
    }

    private fun clearModelCache() {
        CLIENT.itemDefinition_cachedModels.clear()
        CLIENT.npcDefinition_cachedModels.clear()
        CLIENT.spotAnimationDefinition_cachedModels.clear()
        CLIENT.component_cachedModels.clear()
        CLIENT.playerAppearance_cachedModels.clear()
        CLIENT.objectDefinition_cachedModels.clear()
    }
}