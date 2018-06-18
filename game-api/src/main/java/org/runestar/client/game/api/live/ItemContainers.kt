package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.api.NodeHashTable
import org.runestar.client.game.raw.access.XItemContainer
import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeHashTable
import org.runestar.client.game.raw.CLIENT

object ItemContainers : NodeHashTable<Int, ItemContainer>() {

    override val accessor: XNodeHashTable get() = CLIENT.itemContainers

    override fun wrapKey(node: XNode): Int {
        return node.key.toInt()
    }

    override fun unwrapKey(k: Int): Long {
        return k.toLong()
    }

    override fun wrapValue(node: XNode): ItemContainer {
        return ItemContainer(node as XItemContainer)
    }
}