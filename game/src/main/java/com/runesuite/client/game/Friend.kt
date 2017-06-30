package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XFriend
import com.runesuite.client.game.live.Worlds

class Friend(override val accessor: XFriend) : Wrapper() {

    val name: String get() = accessor.name

    val previousName: String? get() = accessor.previousName.takeUnless { it.isNullOrEmpty() }

    val worldId get() = accessor.world

    val world get() = Worlds[accessor.world]

    val rank get() = checkNotNull(ClanMate.Rank.LOOKUP[accessor.rank.toByte()]) { accessor.rank }

    override fun toString(): String {
        return "Friend(name=$name, previousName=$previousName, worldId=$worldId, rank=$rank)"
    }
}