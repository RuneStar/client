package com.runesuite.client.api

import com.runesuite.client.raw.Wrapper
import com.runesuite.client.raw.access.XEntity

abstract class Entity(override val accessor: XEntity) : Wrapper() {

    open val height get() = accessor.height

    abstract val position: Position

    abstract val orientation: Angle

    abstract val model: Model?
}