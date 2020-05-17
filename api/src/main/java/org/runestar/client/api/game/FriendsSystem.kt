package org.runestar.client.api.game

import org.runestar.client.raw.access.XFriendSystem

inline class FriendsSystem(val accessor: XFriendSystem) {

    val friendsList: FriendsList get() = FriendsList(accessor.friendsList)

    val ignoreList: IgnoreList get() = IgnoreList(accessor.ignoreList)
}