package org.runestar.client.plugins.std

import org.runestar.client.api.frame
import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings
import java.awt.Dimension

class WindowSize : Plugin<WindowSize.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        frame.size = settings.size
    }

    class Settings : PluginSettings() {

        val size: Dimension = frame.preferredSize
    }
}