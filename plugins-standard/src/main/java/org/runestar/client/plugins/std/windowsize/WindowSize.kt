package org.runestar.client.plugins.std.windowsize

import org.kxtra.slf4j.logger.info
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
            val insets = Application.frame.insets
            Application.frame.size = Dimension(
                    settings.size.width + insets.left + insets.right,
                    settings.size.height + insets.top + insets.bottom
            )
        }
        logger.info { "frame size: ${Application.frame.size}" }
    }

    data class Settings(
            val size: Dimension = Client.accessor.canvas.preferredSize,
            val includeFrame: Boolean = false
    ) : PluginSettings()
}