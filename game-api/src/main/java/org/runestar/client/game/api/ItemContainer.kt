package org.runestar.client.game.api

import org.runestar.client.game.raw.Wrapper
import org.runestar.client.game.raw.access.XItemContainer

class ItemContainer(override val accessor: XItemContainer) : Wrapper() {

    val size get() = accessor.ids.size

    operator fun get(slot: Int): Item? {
        val id = accessor.ids.getOrNull(slot) ?: return null
        val quantity = accessor.quantities.getOrNull(slot) ?: return null
        return Item.of(id, quantity)
    }

    fun get() = List(size) { get(it) }

    val all get() = get().filterNotNull()

    override fun toString(): String {
        return "ItemContainer(${get()})"
    }
}