package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XFriend
import org.runestar.client.game.raw.access.XFriendsList
import org.runestar.client.game.raw.access.XUser

class FriendsList(override val accessor: XFriendsList) : UserList<Friend>(accessor) {

    override fun wrap(user: XUser): Friend {
        return Friend(user as XFriend)
    }
}