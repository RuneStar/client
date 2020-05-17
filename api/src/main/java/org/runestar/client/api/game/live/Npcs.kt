package org.runestar.client.api.game.live

import org.runestar.client.api.game.Npc
import org.runestar.client.raw.CLIENT

object Npcs : AbstractCollection<Npc>() {

    override fun iterator() = iterator {
        var i = 0
        while (i < size) {
            val npc = get(CLIENT.npcIndices[i++]) ?: break
            yield(npc)
        }
    }

    override val size get() = CLIENT.npcCount

    operator fun get(index: Int): Npc? = CLIENT.npcs[index]?.let { Npc(it, index, Game.plane) }
}