package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNpc

class Npc(
        override val accessor: XNpc,
        val index: Int,
        override val plane: Int
) : Actor(accessor) {

    val type: NpcDefinition? get() = accessor.type?.let { NpcDefinition(it) }
}