package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XIgnoreList
import org.runestar.client.game.raw.access.XIgnored
import org.runestar.client.game.raw.access.XUser

class IgnoreList(override val accessor: XIgnoreList) : UserList<Ignored>(accessor) {

    override fun wrap(user: XUser): Ignored {
        return Ignored(user as XIgnored)
    }
}