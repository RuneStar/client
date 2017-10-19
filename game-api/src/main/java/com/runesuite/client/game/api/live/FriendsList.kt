package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Friend
import com.runesuite.client.game.raw.Client.accessor

object FriendsList {

    val SIZE = accessor.friendsList.size

    fun get(): List<Friend?> = accessor.friendsList.map { it?.let { Friend(it) } }

    val all: List<Friend> get() = accessor.friendsList.mapNotNull { it?.let { Friend(it) } }
}