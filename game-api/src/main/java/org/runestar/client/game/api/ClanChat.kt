package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XClanChat

class ClanChat(override val accessor: XClanChat) : UserList(accessor) {

    val name: String? get() = accessor.name

    val owner: String? get() = accessor.owner
}