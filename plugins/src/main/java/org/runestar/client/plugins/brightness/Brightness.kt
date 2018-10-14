package org.runestar.client.plugins.brightness

import io.reactivex.rxkotlin.subscribeBy
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.VarpId
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings

class Brightness : AbstractPlugin<Brightness.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        setBrightness(settings.brightness)
    }

    override fun stop() {
        setBrightness(1.0 - Game.varps[VarpId.BRIGHTNESS] * 0.1)
    }

    private fun setBrightness(brightness: Double) {
        XClient.doCycle.enter.any { Game.state == GameState.LOGGED_IN }.subscribeBy {
            CLIENT.sprite_cached.clear()
            CLIENT.Rasterizer3D_setBrightness(brightness)
            CLIENT.textureProvider.setBrightness(brightness)
        }
    }

    class Settings(
            val brightness: Double = 0.5
    ) : PluginSettings()
}