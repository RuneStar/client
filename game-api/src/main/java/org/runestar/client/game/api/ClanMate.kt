package org.runestar.client.game.api

import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.Wrapper
import org.runestar.client.game.raw.access.XClanMate

class ClanMate(override val accessor: XClanMate) : Wrapper() {

    val name: String get() = accessor.name

    val worldId get() = accessor.world

    val world get() = Worlds[accessor.world]

    val rank get() = ClanRank.LOOKUP.getValue(accessor.rank)

    override fun toString(): String {
        return "ClanMate(name=$name, worldId=$worldId, rank=$rank)"
    }
}