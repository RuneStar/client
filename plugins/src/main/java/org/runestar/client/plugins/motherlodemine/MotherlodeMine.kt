package org.runestar.client.plugins.motherlodemine

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.cacheids.LocId
import org.runestar.client.api.game.HslColor
import org.runestar.client.api.game.ObjectDefinition
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XLocType
import org.runestar.client.api.plugins.PluginSettings

class MotherlodeMine : DisposablePlugin<MotherlodeMine.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Motherlode Mine"

    override fun onStart() {
        add(XLocType.postDecode.exit.subscribe { objectInit(ObjectDefinition(it.instance)) })
        resetLocTypes()
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
        resetLocTypes()
    }

    private fun resetLocTypes() {
        CLIENT.locType_cached.clear()
        CLIENT.locType_cachedModels.clear()
    }

    private fun isVein(id: Int): Boolean {
        return when (id) {
            LocId.ORE_VEIN_26661,
            LocId.ORE_VEIN_26662,
            LocId.ORE_VEIN_26663,
            LocId.ORE_VEIN_26664 -> true
            else -> false
        }
    }

    class Settings(
            val veinColor: Short = 127
    ) : PluginSettings()
}