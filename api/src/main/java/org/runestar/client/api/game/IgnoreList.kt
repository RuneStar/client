package org.runestar.client.api.game

import org.runestar.client.raw.access.XIgnoreList
import org.runestar.client.raw.access.XIgnored
import org.runestar.client.raw.access.XUser

class IgnoreList(override val accessor: XIgnoreList) : UserList<Ignored>(accessor) {

    override fun wrap(user: XUser): Ignored {
        return Ignored(user as XIgnored)
    }
}