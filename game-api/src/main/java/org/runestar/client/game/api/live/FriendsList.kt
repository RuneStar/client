package org.runestar.client.game.api.live

import org.runestar.client.game.api.Friend
import org.runestar.client.game.raw.Client.accessor

object FriendsList {

    val CAPACITY = accessor.friendsList.size

    fun get(): List<Friend?> = accessor.friendsList.map { it?.let { Friend(it) } }

    val all: List<Friend> get() = accessor.friendsList.mapNotNull { it?.let { Friend(it) } }
}