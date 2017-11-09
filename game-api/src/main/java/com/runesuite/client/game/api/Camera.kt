package com.runesuite.client.game.api

interface Camera {

    val x: Int

    val y: Int

    val z: Int

    val pitch: Angle

    val yaw: Angle

    fun copyOf(): Copy = Copy(x, y, z, pitch, yaw)

    data class Copy(
            override val x: Int,
            override val y: Int,
            override val z: Int,
            override val pitch: Angle,
            override val yaw: Angle
    ) : Camera
}