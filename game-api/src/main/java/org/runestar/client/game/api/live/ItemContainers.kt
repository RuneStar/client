package org.runestar.client.game.api.live

import org.runestar.client.game.api.ItemContainer
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.game.raw.access.XItemContainer
import org.runestar.client.game.raw.access.XNode

object ItemContainers : AbstractMap<Int, ItemContainer>() {

    // Long keys?

    override fun get(key: Int): ItemContainer? {
        return accessor.itemContainers.get(key.toLong())?.let { ItemContainer(it as XItemContainer) }
    }

    override val entries: Set<Map.Entry<Int, ItemContainer>> = object : AbstractSet<Map.Entry<Int, ItemContainer>>() {

        override val size get() = accessor.itemContainers.size // not actual size, just number of buckets

        override fun iterator() = object : AbstractIterator<Map.Entry<Int, ItemContainer>>() {

            private var index = 0

            private lateinit var curr: XNode

            override fun computeNext() {
                if (index > 0 && curr != accessor.itemContainers.buckets[index - 1]) {
                    setNext(makeEntry(curr))
                    curr = curr.previous
                    return
                }
                while (index < accessor.itemContainers.size) {
                    val p = accessor.itemContainers.buckets[index++].previous
                    if (p != accessor.itemContainers.buckets[index - 1]) {
                        curr = p.previous
                        return setNext(makeEntry(p))
                    }
                }
                done()
            }

            private fun makeEntry(node: XNode): Map.Entry<Int, ItemContainer> {
                return java.util.AbstractMap.SimpleImmutableEntry(node.key.toInt(), ItemContainer(node as XItemContainer))
            }
        }
    }
}