package com.runesuite.client.game.api

import java.awt.Point

interface Minimap {

    val zoom: Int

    val orientation: Angle

    val reference: Position

    val center: Point

    fun copyOf(): Copy = Copy(zoom, orientation, reference, center)

    data class Copy(
            override val zoom: Int,
            override val orientation: Angle,
            override val reference: Position,
            override val center: Point
    ) : Minimap
}

