package com.runesuite.client.game.api

import com.runesuite.client.game.raw.access.XNpc

class Npc(override val accessor: XNpc) : Actor(accessor) {

    val definition: NpcDefinition get() = NpcDefinition(accessor.definition)
}