package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XItemContainer

object ItemContainers {

    operator fun get(id: Int): ItemContainer? =
            accessor.itemContainers.get(id.toLong())?.let { ItemContainer(it as XItemContainer) }

    val all get(): Map<Int, ItemContainer> {
        val table = accessor.itemContainers
        val map = mutableMapOf<Int, ItemContainer>()
        var node = table.first() as XItemContainer?
        while (node != null) {
            map[node.key.toInt()] = ItemContainer(node)
            node = table.next() as XItemContainer?
        }
        return map
    }
}