package org.runestar.client.plugins.std.windowsize

import org.kxtra.slf4j.logger.info
import org.kxtra.swing.dimension.minus
import org.kxtra.swing.dimension.plus
import org.runestar.client.api.Application
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings
import java.awt.Dimension

class WindowSize : Plugin<WindowSize.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Window Size"

    override fun start() {
        super.start()
        if (settings.includeFrame) {
            Application.frame.size = settings.size
        } else {
            val frameSize = Application.frame.size - Application.frame.contentPane.size
            Application.frame.size = settings.size + frameSize
        }
        logger.info { "frame size: ${Application.frame.size}, game size: ${Application.frame.contentPane.size}" }
    }

    data class Settings(
            val size: Dimension = Client.accessor.canvas.preferredSize,
            val includeFrame: Boolean = false
    ) : PluginSettings()
}