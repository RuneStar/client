package org.runestar.client.plugins.motherlodemine

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.HslColor
import org.runestar.client.game.api.ObjectDefinition
import org.runestar.client.game.api.ObjectId
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XObjectDefinition
import org.runestar.client.plugins.spi.PluginSettings

class MotherlodeMine : DisposablePlugin<MotherlodeMine.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Motherlode Mine"

    override fun onStart() {
        add(XObjectDefinition.init.exit.subscribe { objectInit(ObjectDefinition(it.instance)) })
        resetObjectDefinitions()
    }

    private fun objectInit(def: ObjectDefinition) {
        if (!isVein(def.id)) return
        val replaceColor = HslColor(settings.veinColor)
        def.recolor(HslColor(6550), replaceColor)
        def.recolor(HslColor(5524), replaceColor)
        def.recolor(HslColor(6930), replaceColor)
        def.recolor(HslColor(7952), replaceColor)
        def.recolor(HslColor(6674), replaceColor)
    }

    override fun onStop() {
        resetObjectDefinitions()
    }

    private fun resetObjectDefinitions() {
        CLIENT.objectDefinition_cached.clear()
        CLIENT.objectDefinition_cachedModels.clear()
    }

    private fun isVein(id: Int): Boolean {
        return when (id) {
            ObjectId.ORE_VEIN_26661,
            ObjectId.ORE_VEIN_26662,
            ObjectId.ORE_VEIN_26663,
            ObjectId.ORE_VEIN_26664 -> true
            else -> false
        }
    }

    class Settings(
            val veinColor: Short = 127
    ) : PluginSettings()
}