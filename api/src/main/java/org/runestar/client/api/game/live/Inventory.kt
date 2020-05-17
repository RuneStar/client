package org.runestar.client.api.game.live

import org.runestar.client.api.game.ItemContainer
import org.runestar.client.api.game.ItemContainerId
import org.runestar.client.api.game.Component
import org.runestar.client.api.game.ComponentId

object Inventory {

    val component: Component? get() = Components[ComponentId.INVENTORY_ITEMS]

    val container: ItemContainer? get() = ItemContainers[ItemContainerId.INVENTORY]

    const val size = 28
}