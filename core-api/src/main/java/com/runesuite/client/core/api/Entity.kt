package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Wrapper
import com.runesuite.client.core.raw.access.XEntity

abstract class Entity(override val accessor: XEntity) : Wrapper() {

    open val height get() = accessor.height

    abstract val position: com.runesuite.client.core.api.Position

    abstract val orientation: com.runesuite.client.core.api.Angle

    abstract val model: com.runesuite.client.core.api.Model?
}