package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XFriendSystem

class FriendsSystem(val accessor: XFriendSystem) {

    val friendsList: FriendsList get() = FriendsList(accessor.friendsList)

    val ignoreList: IgnoreList get() = IgnoreList(accessor.ignoreList)
}