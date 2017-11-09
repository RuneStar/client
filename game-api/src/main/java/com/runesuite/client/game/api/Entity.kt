package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XEntity

abstract class Entity(override val accessor: XEntity) : Wrapper() {

    open val height get() = accessor.height

    abstract val position: Position

    abstract val orientation: com.runesuite.client.game.api.Angle
}