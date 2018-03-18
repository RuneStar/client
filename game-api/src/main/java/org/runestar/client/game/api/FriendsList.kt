package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XFriend
import org.runestar.client.game.raw.access.XFriendsList

class FriendsList(override val accessor: XFriendsList) : UserList<Friend>(accessor) {

    override fun get(index: Int): Friend {
        return Friend(accessor.get(index) as XFriend)
    }
}