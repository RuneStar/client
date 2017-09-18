package com.runesuite.client.api

import com.runesuite.client.raw.access.XGroundItem

class GroundItem(override val accessor: XGroundItem, val location: SceneTile) : Entity(accessor) {

    override val orientation get() = Angle(0)

    override val position get() = location.center

    override val model: Model? get() = accessor.model?.let { Model(position, orientation, it) }

    val id get() = accessor.id

    val quantity get() = accessor.quantity

    override fun toString(): String {
        return "GroundItem(id=$id, quantity=$quantity, location=$location)"
    }
}