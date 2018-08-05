package org.runestar.client.game.api.live

import org.runestar.client.game.api.Npc
import org.runestar.client.game.raw.CLIENT

object Npcs : AbstractCollection<Npc>() {

    override fun iterator() = object : AbstractIterator<Npc>() {

        private var i = 0

        override fun computeNext() {
            if (i >= size) return done()
            val npc = get(CLIENT.npcIndices[i++]) ?: return done()
            setNext(npc)
        }
    }

    override val size get() = CLIENT.npcCount

    operator fun get(index: Int): Npc? = CLIENT.npcs[index]?.let { Npc(it, index, Game.plane) }
}