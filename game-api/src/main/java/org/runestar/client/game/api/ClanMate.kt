package org.runestar.client.game.api

import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.access.XClanMate

class ClanMate(val accessor: XClanMate) {

    val name: String get() = accessor.name()

    val worldId get() = accessor.world

    val world get() = Worlds[accessor.world]

    val rank get() = ClanRank.of(accessor.rank)

    override fun toString(): String {
        return "ClanMate(name=$name, worldId=$worldId, rank=$rank)"
    }
}