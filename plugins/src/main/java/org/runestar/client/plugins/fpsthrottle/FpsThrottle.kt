package org.runestar.client.plugins.fpsthrottle

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.spi.PluginSettings

class FpsThrottle : DisposablePlugin<FpsThrottle.Settings>() {

    override val defaultSettings = Settings()

    override val name = "FPS Throttle"

    override fun onStart() {
        if (settings.sleepTimeMs <= 0) return
        add(XRasterProvider.drawFull0.exit.subscribe {
            if (!CLIENT.canvas.isFocusOwner || !settings.onlyWhenUnfocused) {
                Thread.sleep(settings.sleepTimeMs)
            }
        })
    }

    data class Settings(
            val onlyWhenUnfocused: Boolean = true,
            val sleepTimeMs: Long = 50L
    ) : PluginSettings()
}