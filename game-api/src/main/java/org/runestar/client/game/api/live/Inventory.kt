package org.runestar.client.game.api.live

import org.runestar.client.game.api.Item
import org.runestar.client.game.api.ItemContainerId
import org.runestar.client.game.api.WidgetGroupId
import java.util.*

object Inventory : AbstractList<Item?>(), RandomAccess {

    val widget get() = Widgets[WidgetGroupId.Inventory.items]

    val container get() = ItemContainers[ItemContainerId.INVENTORY]

    override fun get(index: Int): Item? = container?.get(index)

    override val size = 28
}