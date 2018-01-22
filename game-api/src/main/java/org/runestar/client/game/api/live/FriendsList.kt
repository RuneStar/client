package org.runestar.client.game.api.live

import org.runestar.client.game.api.Friend
import org.runestar.client.game.raw.Client.accessor

object FriendsList : AbstractList<Friend>(), RandomAccess {

    override val size get() = accessor.friendsListCount

    val capactiy = accessor.friendsList.size

    override fun get(index: Int) = Friend(checkNotNull(accessor.friendsList[index]))
}