package org.runestar.client.game.api

import java.awt.Point

interface Minimap {

    val zoom: Int

    val orientation: Angle

    val reference: Position

    val center: Point
}

