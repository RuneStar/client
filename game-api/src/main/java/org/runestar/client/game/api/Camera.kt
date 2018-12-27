package org.runestar.client.game.api

interface Camera {

    val localX: Int

    val localY: Int

    val absoluteHeight: Int

    val height: Int

    val plane: Int

    val position: Position get() = Position(localX, localY, height, plane)

    val pitch: Angle

    val yaw: Angle
}