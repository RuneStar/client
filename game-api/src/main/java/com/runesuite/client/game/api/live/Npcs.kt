package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Npc
import com.runesuite.client.game.raw.Client.accessor

object Npcs {

    val SIZE = accessor.npcs.size

    val all: List<com.runesuite.client.game.api.Npc> get() = accessor.npcs.mapNotNull { it?.let { com.runesuite.client.game.api.Npc(it) } }

    fun get(): List<com.runesuite.client.game.api.Npc?> =  accessor.npcs.map { it?.let { com.runesuite.client.game.api.Npc(it) } }

    operator fun get(index: Int): com.runesuite.client.game.api.Npc? = accessor.npcs[index]?.let { com.runesuite.client.game.api.Npc(it) }
}