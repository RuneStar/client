package org.runestar.client.game.api

interface Camera {

    val position: Position

    val pitch: Angle

    val yaw: Angle
}