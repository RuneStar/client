package org.runestar.client.game.api.live

import org.runestar.client.game.api.Item
import org.runestar.client.game.api.ItemContainerId
import org.runestar.client.game.api.WidgetGroupId

object Inventory {

    const val SIZE = 28

    val widget get() = Widgets[WidgetGroupId.Inventory.items]

    val container get() = ItemContainers[ItemContainerId.INVENTORY]

    operator fun get(slot: Int): Item? = container?.get(slot)
}