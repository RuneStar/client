package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings

class ReduceVisibility : DisposablePlugin<ReduceVisibility.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        adjustVisibilityMap()
        add(XClient.Scene_buildVisiblityMap.exit.subscribe { adjustVisibilityMap() })
    }

    private fun adjustVisibilityMap() {
        val amt = settings.amount
        val vm = CLIENT.visibilityMap
        for (pitch in 0 until vm.size) {
            val atPitch = vm[pitch]
            for (yaw in 0 until atPitch.size) {
                val atYaw = atPitch[yaw]
                val maxX = atYaw.size - 1
                for (x in 0..maxX) {
                    val atX = atYaw[x]
                    val maxY = atX.size - 1
                    for (y in 0..maxY) {
                        if (x - amt < 0 || x + amt > maxX || y - amt < 0 || y + amt > maxY) {
                            atX[y] = false
                        }
                    }
                }
            }
        }
    }

    class Settings(
            val amount: Int = 10
    ) : PluginSettings()
}