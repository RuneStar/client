package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XFriend

class Friend(override val accessor: XFriend) : Buddy(accessor) {

    override fun toString(): String {
        return "Friend(username=$username, previousUsername=$previousUsername, worldId=$worldId, rank=$rank)"
    }
}