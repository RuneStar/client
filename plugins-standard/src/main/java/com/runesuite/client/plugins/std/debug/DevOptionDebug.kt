package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.plugins.Plugin
import com.runesuite.client.plugins.PluginSettings

class DevOptionDebug : Plugin<DevOptionDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        if (settings.boundingBoxes2D.draw) {
            accessor.drawBoundingBoxes2D = true
        }
        if (settings.boundingBoxes3D.draw) {
            accessor.drawBoundingBoxes3D = true
        }
        if (settings.boundingBoxes3D.drawAll) {
            accessor.boundingBox3DDrawMode = accessor.boundingBox3DDrawMode_all
        } else {
            accessor.boundingBox3DDrawMode = accessor.boundingBox3DDrawMode_mouseOver
        }
        if (settings.displyFps) {
            accessor.displayFps = true
        }
        if (!settings.useBoundingBoxes3D) {
            accessor.useBoundingBoxes3D = false
        }
        if (settings.boundingBoxes2D.drawObjectGeometry) {
            accessor.drawObjectGeometry2D = true
        }
    }

    override fun stop() {
        super.stop()
        accessor.drawBoundingBoxes2D = false
        accessor.drawBoundingBoxes3D = false
        accessor.displayFps = false
        accessor.useBoundingBoxes3D = true
        accessor.drawObjectGeometry2D = false
    }

    class Settings : PluginSettings() {

        val boundingBoxes3D = ThreeD()
        val boundingBoxes2D = TwoD()
        val displyFps = false
        val useBoundingBoxes3D = true

        class ThreeD {
            val draw = false
            val drawAll = true
        }

        class TwoD {
            val draw = false
            val drawObjectGeometry = false
        }
    }
}