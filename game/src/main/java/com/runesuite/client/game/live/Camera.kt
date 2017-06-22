package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Angle

interface Camera {

    val x: Int

    val y: Int

    val z: Int

    val pitch: Angle

    val yaw: Angle

    object Live : Camera {

        override val x get() = accessor.cameraX

        override val y get() = accessor.cameraY

        override val z get() = accessor.cameraZ

        override val pitch get() = Angle(accessor.cameraPitch)

        override val yaw get() = Angle(accessor.cameraYaw)

        override fun toString(): String {
            return "Camera.Live(x=$x, y=$y, z=$z, pitch=$pitch, yaw=$yaw)"
        }
    }

    fun copyOf(): Copy = Copy(x, y, z, pitch, yaw)

    data class Copy(
            override val x: Int,
            override val y: Int,
            override val z: Int,
            override val pitch: Angle,
            override val yaw: Angle
    ) : Camera
}