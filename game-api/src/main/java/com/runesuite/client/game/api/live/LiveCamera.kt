package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Angle
import com.runesuite.client.game.api.Camera
import com.runesuite.client.game.raw.Client.accessor

object LiveCamera : Camera {

    override val x get() = accessor.cameraX

    override val y get() = accessor.cameraY

    override val z get() = accessor.cameraZ

    override val pitch get() = Angle(accessor.cameraPitch)

    override val yaw get() = Angle(accessor.cameraYaw)

    override fun toString(): String {
        return "LiveCamera(x=$x, y=$y, z=$z, pitch=$pitch, yaw=$yaw)"
    }
}