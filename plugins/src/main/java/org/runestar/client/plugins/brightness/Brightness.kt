package org.runestar.client.plugins.brightness

import io.reactivex.rxjava3.kotlin.subscribeBy
import org.runestar.client.api.game.VarpId
import org.runestar.client.api.game.live.Vars
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.AbstractPlugin
import org.runestar.client.api.plugins.PluginSettings

class Brightness : AbstractPlugin<Brightness.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        setBrightness(settings.brightness)
    }

    override fun stop() {
        setBrightness(1.0 - Vars.getVarp(VarpId.BRIGHTNESS) * 0.1)
    }

    private fun setBrightness(brightness: Double) {
        XClient.doCycleLoggedIn.enter.firstOrError().subscribeBy {
            CLIENT.sprite_cached.clear()
            CLIENT.Rasterizer3D_setBrightness(brightness)
            CLIENT.textureProvider.setBrightness(brightness)
        }
    }

    class Settings(
            val brightness: Double = 0.5
    ) : PluginSettings()
}