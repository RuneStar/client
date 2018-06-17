package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XUser

abstract class User(open val accessor: XUser) : Comparable<User> {

    val username: Username? get() = accessor.username ?.let { Username(it) }

    val previousUsername: Username? get() = accessor.previousUsername ?.let { Username(it) }

    override fun compareTo(other: User) = accessor.compareTo(other.accessor)
}