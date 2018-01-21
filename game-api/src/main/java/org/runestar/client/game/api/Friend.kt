package org.runestar.client.game.api

import org.runestar.client.game.api.live.Worlds
import org.runestar.client.game.raw.access.XFriend

class Friend(val accessor: XFriend) {

    val name: String get() = accessor.name

    val previousName: String? get() = accessor.previousName.takeUnless { it.isNullOrEmpty() }

    val worldId get() = accessor.world

    val world get() = Worlds[accessor.world]

    val rank get() = ClanRank.of(accessor.rank.toByte())

    override fun toString(): String {
        return "Friend(name=$name, previousName=$previousName, worldId=$worldId, rank=$rank)"
    }
}