package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Friend
import com.runesuite.client.game.raw.Client.accessor

object FriendsList {

    val SIZE = accessor.friendsList.size

    fun get(): List<com.runesuite.client.game.api.Friend?> = accessor.friendsList.map { it?.let { com.runesuite.client.game.api.Friend(it) } }

    val all: List<com.runesuite.client.game.api.Friend> get() = accessor.friendsList.mapNotNull { it?.let { com.runesuite.client.game.api.Friend(it) } }
}