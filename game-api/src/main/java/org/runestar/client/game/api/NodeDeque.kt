package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeDeque

abstract class NodeDeque<T, N : XNode>(val accessor: XNodeDeque) : Iterable<T> {

    abstract fun wrap(n: N): T

    override fun iterator() = object : AbstractIterator<T>() {

        private var cur: XNode? = accessor.sentinel.previous

        override fun computeNext() {
            val n = cur
            if (n == null || n == accessor.sentinel) return done()
            @Suppress("UNCHECKED_CAST")
            setNext(wrap(n as N))
            cur = n.previous
        }
    }
}