package org.runestar.client.plugins.windowsize

import org.kxtra.slf4j.logger.info
import org.runestar.client.api.Application
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Component
import java.awt.Dimension

class WindowSize : AbstractPlugin<WindowSize.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Window Size"

    override fun start() {
        (Client.accessor as Component).size = ctx.settings.size
        Application.frame.refit()
        ctx.logger.info { "frame size: ${Application.frame.size}" }
    }

    override fun stop() {

    }

    data class Settings(
            val size: Dimension = Client.accessor.canvas.size
    ) : PluginSettings()
}