package com.runesuite.client.core.api.live

import com.runesuite.client.core.api.Friend
import com.runesuite.client.core.raw.Client.accessor

object FriendsList {

    val SIZE = accessor.friendsList.size

    fun get(): List<Friend?> = accessor.friendsList.map { it?.let { Friend(it) } }

    val all: List<Friend> get() = accessor.friendsList.mapNotNull { it?.let { Friend(it) } }
}