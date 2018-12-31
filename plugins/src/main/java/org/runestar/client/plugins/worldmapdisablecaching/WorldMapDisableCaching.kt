package org.runestar.client.plugins.worldmapdisablecaching

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.WidgetGroupId
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XWidgetGroupParent
import org.runestar.client.plugins.spi.PluginSettings

class WorldMapDisableCaching : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "World Map Disable Caching"

    override fun onStart() {
        add(XClient.closeWidgetGroup.exit.subscribe {
            val wgp = it.arguments[0] as XWidgetGroupParent
            if (wgp.group == WidgetGroupId.WORLD_MAP) {
                val wm = CLIENT.worldMap
                wm.initializeWorldMapManager(wm.worldMapData)
                System.gc()
            }
        })
    }
}