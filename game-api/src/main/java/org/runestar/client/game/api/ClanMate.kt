package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XClanMate

class ClanMate(override val accessor: XClanMate) : Buddy(accessor) {

    override fun toString(): String {
        return "ClanMate(username=$username, previousUsername=$previousUsername, worldId=$worldId, rank=$rank)"
    }
}