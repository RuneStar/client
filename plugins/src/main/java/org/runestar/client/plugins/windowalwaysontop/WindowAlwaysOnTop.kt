package org.runestar.client.plugins.windowalwaysontop

import org.runestar.client.api.Application
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings
import javax.swing.SwingUtilities

class WindowAlwaysOnTop : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override val name = "Window Always on Top"

    override fun start() {
        setAlwaysOnTop(true)
    }

    override fun stop() {
        setAlwaysOnTop(false)
    }

    private fun setAlwaysOnTop(b: Boolean) {
        SwingUtilities.invokeLater {
            Application.frame.isAlwaysOnTop = b
        }
    }
}