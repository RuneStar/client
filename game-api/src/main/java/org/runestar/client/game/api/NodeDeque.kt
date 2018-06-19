package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeDeque

abstract class NodeDeque<T>(val accessor: XNodeDeque) : Iterable<T> {

    abstract fun wrap(t: XNode): T

    override fun iterator() = object : AbstractIterator<T>() {

        private var cur: XNode? = accessor.sentinel.previous

        override fun computeNext() {
            val t = cur
            if (t == null || t == accessor.sentinel) return done()
            setNext(wrap(t))
            cur = t.previous
        }
    }
}