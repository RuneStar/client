package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.api.ItemContainerId
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetGroupId

object Inventory {

    val widget: Widget.Parent? get() = Widgets[WidgetGroupId.Inventory.items]

    val container: ItemContainer? get() = ItemContainers[ItemContainerId.INVENTORY]

    const val size = 28
}