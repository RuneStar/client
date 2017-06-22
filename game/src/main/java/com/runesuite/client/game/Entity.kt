package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XEntity

abstract class Entity(override val accessor: XEntity) : Wrapper() {

    open val height get() = accessor.height

    abstract val position: Position

    abstract val orientation: Angle

    abstract val model: Model?
}