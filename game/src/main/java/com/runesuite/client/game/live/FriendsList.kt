package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Friend

object FriendsList {

    val SIZE = accessor.friendsList.size

    fun get(): List<Friend?> = accessor.friendsList.map { it?.let { Friend(it) } }

    val all: List<Friend> get() = accessor.friendsList.mapNotNull { it?.let { Friend(it) } }
}