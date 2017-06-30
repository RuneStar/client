package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Npc

object Npcs {

    val SIZE = accessor.npcs.size

    val all: List<Npc> get() = accessor.npcs.mapNotNull { it?.let { Npc(it) } }

    fun get(): List<Npc?> =  accessor.npcs.map { it?.let { Npc(it) } }

    operator fun get(index: Int): Npc? = accessor.npcs[index]?.let { Npc(it) }
}