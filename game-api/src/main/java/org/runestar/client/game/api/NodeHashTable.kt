package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeHashTable

abstract class NodeHashTable<K, V> : AbstractMap<K, V>() {

    abstract val accessor: XNodeHashTable

    protected abstract fun wrapKey(node: XNode): K

    protected abstract fun unwrapKey(k: K): Long

    protected abstract fun wrapValue(node: XNode): V

    override fun get(key: K): V? {
        val longKey = unwrapKey(key)
        val bucketSentinel = accessor.buckets[longKey.toInt() and (accessor.size - 1)]
        var cur = bucketSentinel.previous
        while (cur != null && cur != bucketSentinel) {
            if (cur.key == longKey) {
                return wrapValue(cur)
            }
            cur = cur.previous
        }
        return null
    }

    override fun containsKey(key: K): Boolean {
        return get(key) != null
    }

    override val entries: Set<Map.Entry<K, V>> = object : AbstractSet<Map.Entry<K, V>>() {

        override val size get() = sumBy { 1 }

        override fun iterator() = object : AbstractIterator<Map.Entry<K, V>>() {

            private var index = 0

            private lateinit var curr: XNode

            override fun computeNext() {
                if (index > 0 && curr != accessor.buckets[index - 1]) {
                    setNext(makeEntry(curr))
                    curr = curr.previous
                    return
                }
                while (index < accessor.size) {
                    val p = accessor.buckets[index++].previous
                    if (p != accessor.buckets[index - 1]) {
                        curr = p.previous
                        return setNext(makeEntry(p))
                    }
                }
                done()
            }

            private fun makeEntry(node: XNode): Map.Entry<K, V> {
                return java.util.AbstractMap.SimpleImmutableEntry(wrapKey(node), wrapValue(node))
            }
        }
    }
}