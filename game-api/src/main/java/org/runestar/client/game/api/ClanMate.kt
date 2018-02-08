package org.runestar.client.game.api

import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.access.XClanMate

class ClanMate(override val accessor: XClanMate) : Buddy(accessor) {

    val worldId get() = accessor.world

    val world get() = Worlds[accessor.world]

    val rank get() = ClanRank.of(accessor.rank)

    override fun toString(): String {
        return "ClanMate(username=$username, previousUsername=$previousUsername, worldId=$worldId, rank=$rank)"
    }
}