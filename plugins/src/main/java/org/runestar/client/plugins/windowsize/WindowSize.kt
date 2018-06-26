package org.runestar.client.plugins.windowsize

import org.kxtra.slf4j.logger.info
import org.runestar.client.api.Application
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Component
import java.awt.Dimension
import javax.swing.SwingUtilities

class WindowSize : AbstractPlugin<WindowSize.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Window Size"

    override fun start() {
        SwingUtilities.invokeLater {
            (CLIENT as Component).size = settings.gameSize
            val frame = Application.frame
            frame.refit()
            frame.isResizable = false
            logger.info { "frame size: ${frame.size}" }
        }
    }

    override fun stop() {
        SwingUtilities.invokeLater {
            Application.frame.isResizable = true
        }
    }

    data class Settings(
            val gameSize: Dimension = CLIENT.canvas.size
    ) : PluginSettings()
}