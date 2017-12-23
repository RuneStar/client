package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNpc

class Npc(override val accessor: XNpc) : Actor(accessor) {

    val definition: NpcDefinition get() = NpcDefinition(accessor.definition)
}