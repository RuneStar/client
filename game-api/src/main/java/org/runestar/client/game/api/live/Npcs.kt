package org.runestar.client.game.api.live

import org.runestar.client.game.api.Npc
import org.runestar.client.game.raw.Client.accessor

object Npcs {

    val CAPACITY = accessor.npcs.size

    val all: List<Npc> get() = List(count) { Npc(accessor.npcs[accessor.npcIndices[it]]) }

    val count get() = accessor.npcCount

    val indices: List<Int> get() = accessor.npcIndices.take(count)

    fun get(): List<Npc?> =  accessor.npcs.map { it?.let { Npc(it) } }

    operator fun get(index: Int): Npc? = accessor.npcs.getOrNull(index)?.let { Npc(it) }
}