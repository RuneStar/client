package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XIgnoreList
import org.runestar.client.game.raw.access.XIgnored

class IgnoreList(override val accessor: XIgnoreList) : UserList<Ignored>(accessor) {

    override fun get(index: Int): Ignored {
        return Ignored(accessor.get(index) as XIgnored)
    }
}