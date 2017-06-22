package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Npc

object Npcs {

    val SIZE = accessor.npcs.size

    val all: Sequence<Npc> get() = accessor.npcs.copyOf().asSequence().filterNotNull().map { Npc(it) }

    fun get(): List<Npc?> =  accessor.npcs.copyOf().map { it?.let { Npc(it) } }

    operator fun get(index: Int): Npc? = accessor.npcs[index]?.let { Npc(it) }
}