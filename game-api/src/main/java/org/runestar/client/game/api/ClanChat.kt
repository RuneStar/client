package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XClanChat
import org.runestar.client.game.raw.access.XClanMate
import org.runestar.client.game.raw.access.XUser

class ClanChat(override val accessor: XClanChat) : UserList<ClanMate>(accessor) {

    val name: String? get() = accessor.name

    val owner: String? get() = accessor.owner

    override fun wrap(user: XUser): ClanMate {
        return ClanMate(user as XClanMate)
    }
}