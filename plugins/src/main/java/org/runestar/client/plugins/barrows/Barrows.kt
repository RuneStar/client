package org.runestar.client.plugins.barrows

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveMinimap
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class Barrows : DisposablePlugin<Barrows.Settings>() {

    private companion object {
        const val BARROWS_REGION_X = 55
        const val BARROWS_REGION_Y = 151
    }

    override val defaultSettings = Settings()

    override fun start() {
        if (ctx.settings.drawMinimap) {
            add(Game.ticks.subscribe {
                if (inBarrowsUnderground() && !LiveMinimap.isDrawn) {
                    LiveMinimap.isDrawn = true
                }
            })
        }
    }

    override fun stop() {
        super.stop()
        if (ctx.settings.drawMinimap && inBarrowsUnderground() && LiveMinimap.isDrawn) {
            LiveMinimap.isDrawn = false
        }
    }

    private fun inBarrowsUnderground(): Boolean {
        val region = Players.local?.location?.toGlobalTile()?.region ?: return false
        return region.x == BARROWS_REGION_X && region.y == BARROWS_REGION_Y
    }

    class Settings(
            val drawMinimap: Boolean = true
    ) : PluginSettings()
}