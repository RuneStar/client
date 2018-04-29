package org.runestar.client.plugins.windowsize

import org.kxtra.slf4j.logger.info
import org.runestar.client.api.Application
import org.runestar.client.game.raw.Client
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
            (Client.accessor as Component).size = ctx.settings.gameSize
            val frame = Application.frame
            frame.refit()
            frame.isResizable = false
            ctx.logger.info { "frame size: ${frame.size}" }
        }
    }

    override fun stop() {
        SwingUtilities.invokeLater {
            Application.frame.isResizable = true
        }
    }

    data class Settings(
            val gameSize: Dimension = Client.accessor.canvas.size
    ) : PluginSettings()
}