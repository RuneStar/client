package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XClanMate
import com.runesuite.client.game.live.Worlds

class ClanMate(override val accessor: XClanMate) : Wrapper() {

    val world get() = Worlds[accessor.world]

    val rank get() = checkNotNull(Rank.LOOKUP[accessor.rank]) { accessor.rank }

    enum class Rank(val id: Byte) {

        NONE(-1),
        FRIEND(0),
        RECRUIT(1),
        CORPORAL(2),
        SERGEANT(3),
        LIEUTENANT(4),
        CAPTAIN(5),
        GENERAL(6),
        OWNER(7);

        companion object {
            val LOOKUP = values().associateBy { it.id }
        }
    }
}