package org.runestar.client.plugins.std.lowdetail

import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings

class LowDetail : Plugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Low Detail"

    override fun start() {
        super.start()
        Client.accessor.isLowDetail = true
        Client.accessor.objectDefinition_isLowDetail = true
        Client.accessor.scene_isLowDetail = true
        Client.accessor.isStereo = false
    }

    override fun stop() {
        super.stop()
        Client.accessor.isLowDetail = false
        Client.accessor.objectDefinition_isLowDetail = false
        Client.accessor.scene_isLowDetail = false
        Client.accessor.isStereo = true
    }
}