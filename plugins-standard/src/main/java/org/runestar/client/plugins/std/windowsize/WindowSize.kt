package org.runestar.client.plugins.std.windowsize

import org.runestar.client.api.Application
import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings
import java.awt.Dimension

class WindowSize : Plugin<WindowSize.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Window Size"

    override fun start() {
        super.start()
        Application.frame.size = settings.size
    }

    data class Settings(
            val size: Dimension = Application.frame.size
    ) : PluginSettings()
}