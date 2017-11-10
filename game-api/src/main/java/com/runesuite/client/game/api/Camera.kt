package com.runesuite.client.game.api

interface Camera {

    val position: Position

    val pitch: Angle

    val yaw: Angle

    fun copyOf(): Copy = Copy(position, pitch, yaw)

    data class Copy(
            override val position: Position,
            override val pitch: Angle,
            override val yaw: Angle
    ) : Camera
}