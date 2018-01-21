package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XItemContainer

class ItemContainer(val accessor: XItemContainer) : AbstractList<Item?>(), RandomAccess {

    override val size get() = accessor.ids.size

    override fun get(index: Int): Item? {
        val id = accessor.ids.getOrNull(index) ?: return null
        val quantity = accessor.quantities.getOrNull(index) ?: return null
        return Item.of(id, quantity)
    }

    override fun toString(): String {
        return "ItemContainer(size=$size)"
    }
}