package org.runestar.client.api.util

inline class CyclicCache<K, V>(val delegate: MutableMap<K, Value<V>>) {

    constructor() : this(LinkedHashMap())

    class Value<V>(private val v: V) {

        var isActive: Boolean = true

        fun get() = v.also { isActive = true }
    }

    inline fun get(k: K, loader: (K) -> V): V {
        return delegate[k]?.get() ?: loader(k).also { delegate[k] = Value(it) }
    }

    /**
     * Removes all entries which have not been accessed since the last call to [cycle]
     */
    fun cycle() {
        val vs = delegate.values.iterator()
        while (vs.hasNext()) {
            val v = vs.next()
            if (!v.isActive) {
                vs.remove()
            } else {
                v.isActive = false
            }
        }
    }

    fun clear() = delegate.clear()
}