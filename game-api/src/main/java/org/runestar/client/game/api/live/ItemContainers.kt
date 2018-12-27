package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.api.NodeHashTable
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XItemContainer
import org.runestar.client.game.raw.access.XNodeHashTable

object ItemContainers : NodeHashTable<Int, ItemContainer, XItemContainer>() {

    override val accessor: XNodeHashTable get() = CLIENT.itemContainers

    override fun wrapKey(node: XItemContainer): Int = node.key.toInt()

    override fun unwrapKey(k: Int): Long = k.toLong()

    override fun wrapValue(node: XItemContainer): ItemContainer = ItemContainer(node)
}