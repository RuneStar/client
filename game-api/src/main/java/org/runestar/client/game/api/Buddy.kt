package org.runestar.client.game.api

import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.access.XBuddy

abstract class Buddy(override val accessor: XBuddy) : User(accessor) {

    val worldId get() = accessor.world

    val world get() = Worlds[worldId]

    val rank: ClanRank? get() = ClanRank.of(accessor.rank)
}