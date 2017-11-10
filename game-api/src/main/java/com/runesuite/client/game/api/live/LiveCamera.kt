package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Angle
import com.runesuite.client.game.api.Camera
import com.runesuite.client.game.api.Position
import com.runesuite.client.game.raw.Client.accessor

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