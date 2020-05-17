package org.runestar.client.api.game

import org.runestar.client.raw.access.XUser

abstract class User(open val accessor: XUser) : Comparable<User> {

    val username: Username? get() = accessor.username0 ?.let { Username(it) }

    val previousUsername: Username? get() = accessor.previousUsername ?.let { Username(it) }

    override fun compareTo(other: User) = accessor.compareTo(other.accessor)
}