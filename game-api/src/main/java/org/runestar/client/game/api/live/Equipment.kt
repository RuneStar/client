package org.runestar.client.game.api.live

import org.runestar.client.game.api.EquipmentSlot
import org.runestar.client.game.api.Item
import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.api.ItemContainerId

object Equipment {

    const val SIZE = 14

    const val COUNT = 11

    val container: ItemContainer? get() = ItemContainers[ItemContainerId.EQUIPMENT]

    operator fun get(slot: EquipmentSlot): Item? = container?.get(slot.id)
}