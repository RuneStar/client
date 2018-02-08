package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XBuddy

abstract class Buddy(open val accessor: XBuddy): Comparable<Buddy> {

    val username: Username? get() = accessor.username ?.let { Username(it) }

    val previousUsername: Username? get() = accessor.previousUsername ?.let { Username(it) }

    override fun compareTo(other: Buddy) = accessor.compareTo(other.accessor)
}