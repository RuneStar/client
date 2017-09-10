package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor

object Npcs {

    val SIZE = accessor.npcs.size

    val all: List<com.runesuite.client.core.api.Npc> get() = accessor.npcs.mapNotNull { it?.let { com.runesuite.client.core.api.Npc(it) } }

    fun get(): List<com.runesuite.client.core.api.Npc?> =  accessor.npcs.map { it?.let { com.runesuite.client.core.api.Npc(it) } }

    operator fun get(index: Int): com.runesuite.client.core.api.Npc? = accessor.npcs[index]?.let { com.runesuite.client.core.api.Npc(it) }
}