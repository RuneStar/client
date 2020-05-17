package org.runestar.client.api.game.live

import org.runestar.client.api.game.ItemContainer
import org.runestar.client.api.game.NodeHashTable
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XInventory
import org.runestar.client.raw.access.XNodeHashTable

object ItemContainers : NodeHashTable<Int, ItemContainer, XInventory>() {

    override val accessor: XNodeHashTable get() = CLIENT.itemContainers

    override fun wrapKey(node: XInventory): Int = node.key.toInt()

    override fun unwrapKey(k: Int): Long = k.toLong()

    override fun wrapValue(node: XInventory): ItemContainer = ItemContainer(node)
}