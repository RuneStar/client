package org.runestar.client.game.api.live

import org.runestar.client.game.api.Angle
import org.runestar.client.game.api.Camera
import org.runestar.client.game.api.Position
import org.runestar.client.game.raw.Client.accessor

object LiveCamera : Camera {

    override val position get() = Position(accessor.cameraX, accessor.cameraZ, accessor.cameraY, accessor.plane).run {
        copy(height = LiveScene.getTileHeight(this) - height)
    }

    override val pitch get() = Angle(accessor.cameraPitch)

    override val yaw get() = Angle(accessor.cameraYaw)

    override fun toString(): String {
        return "LiveCamera(position=$position, pitch=$pitch, yaw=$yaw)"
    }
}