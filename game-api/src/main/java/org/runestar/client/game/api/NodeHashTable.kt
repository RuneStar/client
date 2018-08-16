package org.runestar.client.game.api

import com.google.common.collect.Iterators
import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeHashTable

abstract class NodeHashTable<K, V, N : XNode> : AbstractMap<K, V>(), Iterable<N> {

    abstract val accessor: XNodeHashTable

    protected abstract fun wrapKey(node: N): K

    protected abstract fun unwrapKey(k: K): Long

    protected abstract fun wrapValue(node: N): V

    @Suppress("UNCHECKED_CAST")
    override fun get(key: K): V? {
        val longKey = unwrapKey(key)
        val bucketSentinel = accessor.buckets[longKey.toInt() and (accessor.size - 1)]
        var cur = bucketSentinel.previous
        while (cur != null && cur != bucketSentinel) {
            if (cur.key == longKey) {
                return wrapValue(cur as N)
            }
            cur = cur.previous
        }
        return null
    }

    override fun containsKey(key: K): Boolean {
        return get(key) != null
    }

    override fun iterator(): Iterator<N> = object : AbstractIterator<N>() {

        private var index = 0

        private lateinit var curr: N

        @Suppress("UNCHECKED_CAST")
        override fun computeNext() {
            if (index > 0 && curr != accessor.buckets[index - 1]) {
                setNext(curr)
                curr = curr.previous as N
                return
            }
            while (index < accessor.size) {
                val p = accessor.buckets[index++].previous as N
                if (p != accessor.buckets[index - 1]) {
                    curr = p.previous as N
                    return setNext(p)
                }
            }
            done()
        }
    }

    override val entries: Set<Map.Entry<K, V>> = object : AbstractSet<Map.Entry<K, V>>() {

        override val size get() = (this@NodeHashTable as Iterable<N>).count()

        override fun iterator() = Iterators.transform(this@NodeHashTable.iterator()) { makeEntry(checkNotNull(it)) }

        private fun makeEntry(node: N): Map.Entry<K, V> {
            return java.util.AbstractMap.SimpleImmutableEntry(wrapKey(node), wrapValue(node))
        }
    }
}