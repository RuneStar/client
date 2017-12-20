package com.runesuite.client.game.api

import com.runesuite.client.game.raw.access.XGroundItem

class GroundItem(
        override val accessor: XGroundItem,
        val location: SceneTile
) : Entity(accessor) {

    override val orientation get() = Angle(0)

    override val position get() = location.center

    val id get() = accessor.id

    val quantity get() = accessor.quantity

    override fun toString(): String {
        return "GroundItem(id=$id, quantity=$quantity, location=$location)"
    }
}