package org.runestar.client.game.api

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.raw.access.XNpc

class Npc(override val accessor: XNpc) : Actor(accessor) {

    override val plane = Game.plane // todo

    val definition: NpcDefinition? get() = accessor.definition?.let { NpcDefinition(it) }
}