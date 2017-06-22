package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Friend

object FriendsList {

    val SIZE = accessor.friendsList.size

    fun get(): List<Friend?> = accessor.friendsList.copyOf().map { it?.let { Friend(it) } }

    val all: Sequence<Friend> get() = accessor.friendsList.copyOf().asSequence().filterNotNull().map { Friend(it) }
}