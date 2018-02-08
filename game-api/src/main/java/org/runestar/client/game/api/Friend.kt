package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XFriend

class Friend(override val accessor: XFriend) : Buddy(accessor) {

//    val worldId get() = accessor.

//    val world get() = Worlds[accessor.world]

//    val rank get() = ClanRank.of(accessor.rank.toByte())

    override fun toString(): String {
        return "Friend(username=$username, previousUsername=$previousUsername)"
    }
}