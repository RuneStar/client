package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeDeque

abstract class NodeDeque<T, N : XNode>(val accessor: XNodeDeque) : Iterable<T> {

    abstract fun wrap(n: N): T

    override fun iterator() = iterator {
        var cur: XNode? = accessor.sentinel.previous
        while (cur != null && cur != accessor.sentinel) {
            @Suppress("UNCHECKED_CAST")
            yield(wrap(cur as N))
            cur = cur.previous
        }
    }
}