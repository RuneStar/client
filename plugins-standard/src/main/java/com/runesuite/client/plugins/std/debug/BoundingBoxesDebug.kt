package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.plugins.Plugin
import com.runesuite.client.plugins.PluginSettings

class BoundingBoxesDebug : Plugin<BoundingBoxesDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        if (settings.twoD.on) {
            accessor.drawBoundingBoxes2D = true
        }
        if (settings.aabb.on) {
            accessor.drawAxisAlignedBoundingBoxes = true
        }
        if (settings.aabb.all) {
            accessor.axisAlignedBoundingBoxDrawMode = accessor.axisAlignedBoundingBoxDrawMode_ALL
        } else {
            accessor.axisAlignedBoundingBoxDrawMode = accessor.axisAlignedBoundingBoxDrawMode_MOUSE_OVER
        }
    }

    override fun stop() {
        super.stop()
        accessor.drawBoundingBoxes2D = false
        accessor.drawAxisAlignedBoundingBoxes = false
    }

    class Settings : PluginSettings() {

        val aabb = Aabb()
        val twoD = TwoD()

        class Aabb {
            val on = true
            val all = true
        }

        class TwoD {
            val on = true
        }
    }
}