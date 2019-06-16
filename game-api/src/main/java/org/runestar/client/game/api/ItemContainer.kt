package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XInventory

class ItemContainer(val accessor: XInventory) : AbstractList<Item?>(), RandomAccess {

    val ids: IntArray get() = accessor.ids

    val quantities: IntArray get() = accessor.quantities

    override val size get() = ids.size

    override fun get(index: Int): Item? {
        if (index >= size) return null
        return Item.of(ids[index], quantities[index])
    }
}