package org.runestar.client.plugins.motherlodemine

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.HslColor
import org.runestar.client.game.api.ObjectDefinition
import org.runestar.client.game.api.ObjectId
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XLocType
import org.runestar.client.plugins.spi.PluginSettings

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