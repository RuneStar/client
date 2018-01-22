package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XItemContainer

class ItemContainer(val accessor: XItemContainer) : AbstractList<Item?>(), RandomAccess {

    override val size get() = accessor.ids.size

    override fun get(index: Int): Item? {
        if (index >= size) return null
        return Item.of(accessor.ids[index], accessor.quantities[index])
    }
}