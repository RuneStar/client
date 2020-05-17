package org.runestar.client.api.game

import org.runestar.client.raw.access.XClanMate

class ClanMate(override val accessor: XClanMate) : Buddy(accessor) {

    override fun toString(): String {
        return "ClanMate(username=$username, previousUsername=$previousUsername, worldId=$worldId, rank=$rank)"
    }
}