package org.runestar.client.game.api.live

import org.runestar.client.game.api.Npc
import org.runestar.client.game.raw.Client.accessor

object Npcs : AbstractCollection<Npc>() {

    override fun iterator() = object : AbstractIterator<Npc>() {

        private var i = 0

        override fun computeNext() {
            if (i >= size) return done()
            val x = accessor.npcs[accessor.npcIndices[i++]] ?: return done()
            setNext(Npc(x))
        }
    }

    override val size get() = accessor.npcCount

    internal operator fun get(index: Int): Npc? = accessor.npcs[index]?.let { Npc(it) }
}