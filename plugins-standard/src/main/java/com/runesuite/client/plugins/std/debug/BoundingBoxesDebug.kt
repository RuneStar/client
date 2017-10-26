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
        if (settings.threeD.on) {
            accessor.drawBoundingBoxes3D = true
        }
        if (settings.threeD.all) {
            accessor.boundingBox3DDrawMode = accessor.boundingBox3DDrawMode_all
        } else {
            accessor.boundingBox3DDrawMode = accessor.boundingBox3DDrawMode_mouseOver
        }
    }

    override fun stop() {
        super.stop()
        accessor.drawBoundingBoxes2D = false
        accessor.drawBoundingBoxes3D = false
    }

    class Settings : PluginSettings() {

        val threeD = ThreeD()
        val twoD = TwoD()

        class ThreeD {
            val on = true
            val all = true
        }

        class TwoD {
            val on = true
        }
    }
}