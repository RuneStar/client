package com.runesuite.client.core.api

import com.runesuite.client.core.raw.access.XGroundItem

class GroundItem(override val accessor: XGroundItem, val location: com.runesuite.client.core.api.SceneTile) : com.runesuite.client.core.api.Entity(accessor) {

    override val orientation get() = com.runesuite.client.core.api.Angle(0)

    override val position get() = location.center

    override val model: com.runesuite.client.core.api.Model? get() = accessor.model?.let { com.runesuite.client.core.api.Model(position, orientation, it) }

    val id get() = accessor.id

    val quantity get() = accessor.quantity

    override fun toString(): String {
        return "GroundItem(id=$id, quantity=$quantity, location=$location)"
    }
}