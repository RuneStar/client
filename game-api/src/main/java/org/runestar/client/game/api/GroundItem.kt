package org.runestar.client.game.api

import org.runestar.client.game.api.live.Mouse.location
import org.runestar.client.game.raw.access.XObj

class GroundItem(
        override val accessor: XObj,
        override val modelPosition: Position
) : Entity(accessor) {

    override val orientation get() = Angle.ZERO

    val id get() = accessor.id

    val quantity get() = accessor.quantity

    val item get() = Item(id, quantity)

    override fun toString(): String {
        return "GroundItem(item=$item, location=$location)"
    }

    companion object {

        const val MAX_QUANTITY = 65535
    }
}