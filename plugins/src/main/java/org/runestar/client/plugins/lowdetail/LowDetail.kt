package org.runestar.client.plugins.lowdetail

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings

class LowDetail : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Low Detail"

    override fun start() {
        CLIENT.isLowDetail = true
        CLIENT.scene_isLowDetail = true
    }

    override fun stop() {
        CLIENT.isLowDetail = false
        CLIENT.scene_isLowDetail = false
    }
}