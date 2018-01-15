package org.runestar.client.plugins.std

import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class FpsThrottle : DisposablePlugin<FpsThrottle.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        if (settings.sleepTimeMs <= 0) return
        val sleepTimeMs = settings.sleepTimeMs
        if (settings.onlyWhenUnfocused) {
            add(XRasterProvider.drawFull0.exit.subscribe {
                if (!Client.accessor.canvas.isFocusOwner) {
                    Thread.sleep(sleepTimeMs)
                }
            })
        } else {
            add(XRasterProvider.drawFull0.exit.subscribe {
                Thread.sleep(sleepTimeMs)
            })
        }
    }

    class Settings : PluginSettings() {
        val onlyWhenUnfocused = true
        val sleepTimeMs = 50L
    }
}