package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.api.ItemContainerId
import org.runestar.client.game.api.Component
import org.runestar.client.game.api.ComponentId

object Inventory {

    val component: Component? get() = Components[ComponentId.INVENTORY_ITEMS]

    val container: ItemContainer? get() = ItemContainers[ItemContainerId.INVENTORY]

    const val size = 28
}