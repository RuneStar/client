package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XEntity

abstract class Entity(open val accessor: XEntity) {

    open val height get() = accessor.height

    abstract val position: Position

    abstract val orientation: Angle
}