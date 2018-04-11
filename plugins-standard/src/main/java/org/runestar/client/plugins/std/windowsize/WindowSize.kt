package org.runestar.client.plugins.std.windowsize

import org.kxtra.slf4j.logger.info
import org.runestar.client.api.Application
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.AbstractPlugin
import org.runestar.client.plugins.PluginSettings
import java.awt.Dimension

class WindowSize : AbstractPlugin<WindowSize.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Window Size"

    override fun start() {
        if (ctx.settings.includeFrame) {
            Application.frame.size = ctx.settings.size
        } else {
            val insets = Application.frame.insets
            Application.frame.size = Dimension(
                    ctx.settings.size.width + insets.left + insets.right,
                    ctx.settings.size.height + insets.top + insets.bottom
            )
        }
        ctx.logger.info { "frame size: ${Application.frame.size}" }
    }

    override fun stop() {

    }

    data class Settings(
            val size: Dimension = Client.accessor.canvas.preferredSize,
            val includeFrame: Boolean = false
    ) : PluginSettings()
}