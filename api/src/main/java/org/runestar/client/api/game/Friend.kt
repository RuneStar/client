package org.runestar.client.api.game

import org.runestar.client.raw.access.XFriend

class Friend(override val accessor: XFriend) : Buddy(accessor) {

    override fun toString(): String {
        return "Friend(username=$username, previousUsername=$previousUsername, worldId=$worldId, rank=$rank)"
    }
}