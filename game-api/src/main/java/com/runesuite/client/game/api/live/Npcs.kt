package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Npc
import com.runesuite.client.game.raw.Client.accessor

object Npcs {

    val SIZE = accessor.npcs.size

    val all: List<Npc> get() = accessor.npcs.mapNotNull { it?.let { Npc(it) } }

    fun get(): List<Npc?> =  accessor.npcs.map { it?.let { Npc(it) } }

    operator fun get(index: Int): Npc? = accessor.npcs[index]?.let { Npc(it) }
}