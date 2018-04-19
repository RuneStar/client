package org.runestar.client.plugins.windowalwaysontop

import org.runestar.client.api.Application
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings

class WindowAlwaysOnTop : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Window Always on Top"

    override fun start() {
        Application.frame.isAlwaysOnTop = true
    }

    override fun stop() {
        Application.frame.isAlwaysOnTop = false
    }
}