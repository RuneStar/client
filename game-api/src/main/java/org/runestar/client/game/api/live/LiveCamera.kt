package org.runestar.client.game.api.live

import org.runestar.client.game.api.Angle
import org.runestar.client.game.api.Camera
import org.runestar.client.game.api.Position
import org.runestar.client.game.api.Scene
import org.runestar.client.game.raw.Client.accessor

object LiveCamera : Camera {

    override val position: Position get() {
        val l = Position(accessor.cameraX, accessor.cameraZ, accessor.cameraY, accessor.plane)
        if (!l.isLoaded) {
            return Scene.CENTER.center // todo
        }
        val th = LiveScene.getTileHeight(l)
        return l.copy(height = LiveScene.getTileHeight(l) - th)
    }

    override val pitch get() = Angle(accessor.cameraPitch)

    override val yaw get() = Angle(accessor.cameraYaw)

    override fun toString(): String {
        return "LiveCamera(position=$position, pitch=$pitch, yaw=$yaw)"
    }
}