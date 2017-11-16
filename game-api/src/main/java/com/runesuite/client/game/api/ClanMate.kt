package com.runesuite.client.game.api

import com.runesuite.client.game.api.live.Worlds
import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XClanMate

class ClanMate(override val accessor: XClanMate) : Wrapper() {

    val name: String get() = accessor.name

    val worldId get() = accessor.world

    val world get() = Worlds[accessor.world]

    val rank get() = checkNotNull(ClanRank.LOOKUP[accessor.rank]) { accessor.rank }

    override fun toString(): String {
        return "ClanMate(name=$name, worldId=$worldId, rank=$rank)"
    }
}