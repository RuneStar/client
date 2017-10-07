package com.runesuite.client.game.api

import com.runesuite.client.game.raw.access.XGroundItem

class GroundItem(override val accessor: XGroundItem, val location: com.runesuite.client.game.api.SceneTile) : com.runesuite.client.game.api.Entity(accessor) {

    override val orientation get() = com.runesuite.client.game.api.Angle(0)

    override val position get() = location.center

    override val model: com.runesuite.client.game.api.Model? get() = accessor.model?.let { com.runesuite.client.game.api.Model(position, orientation, it) }

    val id get() = accessor.id

    val quantity get() = accessor.quantity

    override fun toString(): String {
        return "GroundItem(id=$id, quantity=$quantity, location=$location)"
    }
}