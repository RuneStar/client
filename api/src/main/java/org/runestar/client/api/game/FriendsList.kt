package org.runestar.client.api.game

import org.runestar.client.raw.access.XFriend
import org.runestar.client.raw.access.XFriendsList
import org.runestar.client.raw.access.XUser

class FriendsList(override val accessor: XFriendsList) : UserList<Friend>(accessor) {

    override fun wrap(user: XUser): Friend {
        return Friend(user as XFriend)
    }
}