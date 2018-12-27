package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.api.ItemContainerId
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetId

object Inventory {

    val widget: Widget.Inventory? get() = Widgets[WidgetId.INVENTORY_ITEMS] as Widget.Inventory?

    val container: ItemContainer? get() = ItemContainers[ItemContainerId.INVENTORY]

    const val size = 28
}