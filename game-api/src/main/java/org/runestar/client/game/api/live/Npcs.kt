package org.runestar.client.game.api.live

import org.runestar.client.game.api.Npc
import org.runestar.client.game.raw.CLIENT

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