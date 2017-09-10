package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor

object FriendsList {

    val SIZE = accessor.friendsList.size

    fun get(): List<com.runesuite.client.core.api.Friend?> = accessor.friendsList.map { it?.let { com.runesuite.client.core.api.Friend(it) } }

    val all: List<com.runesuite.client.core.api.Friend> get() = accessor.friendsList.mapNotNull { it?.let { com.runesuite.client.core.api.Friend(it) } }
}